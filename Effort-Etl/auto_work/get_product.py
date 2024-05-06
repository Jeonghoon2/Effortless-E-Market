import logging
import random

import requests


class GetProduct:
    def __init__(self):
        """클래스 초기화와 환경 설정."""
        self.http = None

    def setup_env(self, run_env):
        """실행 환경에 따라 API 서버의 기본 URL을 설정합니다."""
        env_config = {
            "prod": "http://10.8.0.10:7070",
            "dev": "http://localhost:7077"
        }
        self.http = env_config.get(run_env, "http://localhost:7077")
        logging.info(f"설정된 환경: {run_env}, API 서버 URL: {self.http}")

    def get_all_product(self):
        """API에서 모든 상품 정보를 조회합니다."""
        try:
            response = requests.get(f"{self.http}/api/v1/product/all/py")
            response.raise_for_status()
            products = response.json()
            logging.info(f"조회된 상품 수: {len(products)}")
            return products
        except requests.RequestException as e:
            logging.error("상품 정보 조회에 실패하였습니다.")
            return []

    def choice_products(self, products):
        """입력된 상품 목록에서 무작위로 상품을 선택하고 세부 정보를 출력합니다."""
        if not products:
            logging.warning("선택할 상품이 없습니다.")
            return

        selected_products = random.sample(products, len(products))
        logging.info(f"선택된 상품 수: {len(selected_products)}")

        for product in selected_products:
            try:
                response = requests.get(f"{self.http}/api/v1/product/{product['id']}")
                response.raise_for_status()
                product_details = response.json()
                logging.info(f"상품 ID {product['id']} 세부 정보: {product_details}")
            except requests.RequestException:
                logging.error(f"상품 ID {product['id']} 세부 정보 조회 실패")

    def main(self, run_env):
        """상품 정보를 가져와 랜덤으로 선택된 상품의 세부 정보를 로깅합니다."""
        logging.basicConfig(level=logging.INFO)
        logging.info("상품 조회 프로세스 시작...")
        self.setup_env(run_env)
        products = self.get_all_product()
        if products:
            self.choice_products(products)

        logging.info("상품 조회 프로세스 완료")


if __name__ == "__main__":
    product_app = GetProduct()
    product_app.main("dev")
