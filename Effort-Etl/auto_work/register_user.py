import json
import logging
import random

import requests
from faker import Faker

fake = Faker("ko_KR")


class RegisterUser:
    def __init__(self):
        self.http = None
        self.headers = {"Content-Type": "application/json"}

    def setup_env(self, run_env):
        """실행 환경에 따라 API 서버의 기본 URL을 설정합니다."""
        env_config = {
            "dev": "http://localhost:7077",
            "prod": "http://10.8.0.10:7070"
        }
        self.http = env_config.get(run_env, "http://localhost:7077")
        logging.info(f"환경 설정: {run_env}, URL: {self.http}")

    def generate_korean_phone_number(self):
        """한국식 휴대폰 번호를 생성합니다."""
        first_part = "010"
        second_part = random.randint(1000, 9999)
        third_part = random.randint(1000, 9999)
        return f"{first_part}-{second_part:04d}-{third_part:04d}"

    def generate_random_data(self):
        """랜덤 사용자 데이터를 생성합니다."""
        data = {
            "email": fake.email(),
            "password": fake.password(),
            "name": fake.name(),
            "gender": random.choice(['M', 'F']),
            "phoneNumber": self.generate_korean_phone_number(),
            "address": fake.address()
        }

        logging.info(f"생성된 사용자 데이터 : {data}")
        return data

    def generate_address(self, user):
        """사용자의 주소 데이터를 생성합니다."""
        user_id = user.json().get('id')
        address_data = {
            "memberId": user_id,
            "recipientName": fake.name(),
            "recipientPhoneNumber": self.generate_korean_phone_number(),
            "recipientAddress": fake.address()
        }
        logging.info(f"생성된 주소 데이터: {address_data}")
        return address_data

    def post_user_data(self, data):
        """사용자 데이터를 API를 통해 등록합니다."""
        response = requests.post(self.http + "/api/v1/member", headers=self.headers, data=json.dumps(data))
        logging.info(f"사용자 등록: {response.json()}")
        return response

    def post_address_data(self, data):
        """사용자 주소 데이터를 API를 통해 등록합니다."""
        response = requests.post(self.http + "/api/v1/member/address", headers=self.headers, data=json.dumps(data))
        logging.info(f"주소 등록: {response.json()}")
        return response

    def main(self, run_env):
        logging.basicConfig(level=logging.INFO)
        logging.info("사용자 등록 프로세스 시작...")
        self.setup_env(run_env)
        user_data = self.generate_random_data()
        created_user = self.post_user_data(user_data)
        address_data = self.generate_address(created_user)
        self.post_address_data(address_data)
        logging.info("사용자 등록 프로세스 완료.")


if __name__ == "__main__":
    user_app = RegisterUser()
    user_app.main("dev")
