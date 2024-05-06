import json
import logging
import random
import time

import requests
from faker import Faker

fake = Faker('ko_KR')


class RegisterSeller:
    def __init__(self):
        self.http = None
        self.headers = {"Content-Type": "application/json"}

    def setup_env(self, run_env):
        """환경 설정에 따라 API 서버의 URL을 설정합니다."""
        env_config = {
            "prod": "http://10.8.0.10:7070",
            "dev": "http://localhost:7077"
        }
        self.http = env_config.get(run_env, "http://localhost:7077")
        logging.info(f"환경 설정됨: {run_env} URL: {self.http}")

    @staticmethod
    def generate_korean_phone_number():
        """한국식 휴대전화 번호를 생성합니다."""
        first_part = "010"
        second_part = random.randint(1000, 9999)
        third_part = random.randint(1000, 9999)
        return f"{first_part}-{second_part:04d}-{third_part:04d}"

    def generate_random_seller_data(self):
        """랜덤 판매자 데이터를 생성합니다."""
        data = {
            "email": fake.email(),
            "password": "1234",  # 비밀번호는 문자열로 처리
            "name": fake.name(),
            "brandName": "shop" + str(time.time_ns()),
            "phoneNumber": self.generate_korean_phone_number()
        }
        logging.info(f"생성된 판매자 데이터: {data}")
        return data

    def post_data(self, data):
        """생성된 판매자 데이터를 API를 통해 등록합니다."""
        json_data = json.dumps(data)
        response = requests.post(f"{self.http}/api/v1/seller", headers=self.headers, data=json_data)
        logging.info(f"판매자 등록 상태: {response.json()}")

    def main(self, run_env):
        logging.basicConfig(level=logging.INFO)

        logging.info("판매자 등록 프로세스 시작...")
        self.setup_env(run_env)
        seller_data = self.generate_random_seller_data()
        self.post_data(seller_data)
        logging.info("판매자 등록 프로세스 완료.")


if __name__ == "__main__":
    seller_app = RegisterSeller()
    seller_app.main("dev")
