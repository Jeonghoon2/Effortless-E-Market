import csv
import logging
import os
import random
from typing import Any

import requests


class RegisterProduct:
    def __init__(self):
        self.http = None
        self.base_path = None
        self.seller = None
        self.first_category = None
        self.second_category = None
        self.third_category = None
        self.third_category_id = None
        self.final_category_path = None

    def setup_env(self, run_env):
        """환경에 따라 API 서버의 기본 경로를 설정합니다."""
        if run_env == "dev":
            self.base_path = "/Users/jeong/Desktop/Effortless-E-Market/E-Market-Crawling/상품/data/item"
            self.http = "http://localhost:7077"
        elif run_env == "prod":
            self.base_path = "/usr/bdp/airflow/dags/sample/item"
            self.http = "http://10.8.0.10:7070"
        logging.info(f"환경 설정됨: {run_env}, 경로: {self.http}")

    def select_seller(self):
        """API를 통해 랜덤 판매자를 선택합니다."""
        response = requests.get(f"{self.http}/api/v1/seller/all")
        if response.status_code == 200:
            self.seller = random.choice(response.json())
            logging.info(f"선택된 판매자: {self.seller['name']} (ID: {self.seller['id']})")
        else:
            logging.error("판매자 정보를 불러오는 데 실패했습니다.")

    def select_category(self):
        """판매자의 카테고리를 선택합니다."""
        first_categories = [d for d in os.listdir(self.base_path) if os.path.isdir(os.path.join(self.base_path, d))]
        self.first_category = random.choice(first_categories)
        first_category_path: Any = os.path.join(self.base_path, self.first_category)

        second_categories = [d for d in os.listdir(first_category_path) if
                             os.path.isdir(os.path.join(first_category_path, d))]
        self.second_category = random.choice(second_categories)
        second_category_path: Any = os.path.join(first_category_path, self.second_category)

        third_categories = [d for d in os.listdir(second_category_path) if
                            os.path.isdir(os.path.join(second_category_path, d))]
        self.third_category = random.choice(third_categories)

        self.final_category_path: Any = os.path.join(second_category_path, self.third_category)
        self.check_and_save_category()

    def check_and_save_category(self):
        """카테고리 존재 유무를 확인하고, 없으면 생성합니다."""
        self.third_category_id = self.get_or_create_category(self.third_category, 2,
                                                             self.get_or_create_category(self.second_category, 1,
                                                                                         self.get_or_create_category(
                                                                                             self.first_category, 0,
                                                                                             None)))

    def get_or_create_category(self, category_name, depth, parent_id):
        """카테고리를 검증하거나 생성하고, 해당 ID를 반환합니다."""
        category_check_url = f"{self.http}/api/v1/category/check"
        category_create_url = f"{self.http}/api/v1/category"
        params = {'depth': depth, 'name': category_name, 'parentId': parent_id}
        headers = {'Content-Type': 'application/json'}
        response = requests.get(category_check_url, json=params)
        if response.status_code == 404:
            category_data = {'name': category_name, 'parentId': parent_id} if parent_id else {'name': category_name}
            response = requests.post(category_create_url, json=category_data, headers=headers)
            return response.json().get('id')
        elif response.status_code == 200:
            return response.json().get('id')
        else:
            logging.error(f"카테고리 조회 실패: {response.json()}")

    def add_product(self):
        """상품을 추가합니다."""
        csv_file_path = os.path.join(self.final_category_path, 'item.csv')
        with open(csv_file_path, 'r', encoding='utf-8') as file:
            products = list(csv.DictReader(file))
            product = random.choice(products)
            self.post_product(product)

    def post_product(self, product):
        """API를 통해 상품 데이터를 등록합니다."""
        image_path = os.path.join(self.final_category_path, product['image_name'].split('?')[0])
        if not os.path.exists(image_path):
            logging.error(f"이미지 파일을 찾을 수 없습니다: {image_path}")
            return
        with open(image_path, 'rb') as image_file:
            files = {'file': (os.path.basename(image_path), image_file, 'image/jpeg')}
            data = {
                'name': product['name'],
                'price': int(product['price'].replace(",", "")),
                'description': "test product description",
                'quantity': random.randint(200, 5000),
                'sellerId': self.seller['id'],
                'categoryId': self.third_category_id
            }
            response = requests.post(f"{self.http}/api/v1/product", files=files, data=data)
            if response.status_code != 201:
                logging.error(f"상품 등록 실패: {response.json()}")

    def main(self, run_env):
        logging.basicConfig(level=logging.INFO)
        logging.info("상품 등록 프로세스 시작...")
        self.setup_env(run_env)
        self.select_seller()
        self.select_category()
        if self.third_category_id:
            self.add_product()
        logging.info("상품 등록 프로세스 완료.")


if __name__ == "__main__":
    app = RegisterProduct()
    app.main("dev")
