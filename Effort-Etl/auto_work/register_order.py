import json
import logging
import random

import requests
from faker import Faker

fake = Faker("ko_KR")


class RegisterOrder:
    def __init__(self):
        """초기 환경 설정을 수행합니다."""
        self.http = None
        self.headers = {"Content-Type": "application/json"}
        self.member_list = None
        self.product_list = None
        self.card_name = ['신한카드', '삼성카드', '비씨카드', '케이비국민카', '현대카드', '롯데카드', '우리카드', '하나카드']

    def setup_env(self, run_env):
        """API 서버의 URL을 환경에 따라 설정합니다."""
        env_config = {
            "prod": "http://10.8.0.10:7070",
            "dev": "http://localhost:7077"
        }
        self.http = env_config.get(run_env, "http://localhost:7077")
        logging.info(f"API 서버 URL 설정됨: {self.http}")

    def get_all_product(self):
        """모든 상품 정보를 API 서버에서 가져옵니다."""
        response = requests.get(self.http + "/api/v1/product/all/py")
        if response.status_code == 200:
            logging.info("모든 상품 정보가 성공적으로 가져와졌습니다.")
            return response.json()
        else:
            logging.error("상품 정보를 가져오는데 실패했습니다.")
            return []

    def get_all_member(self):
        """모든 회원 정보를 API 서버에서 가져옵니다."""
        response = requests.get(self.http + "/api/v1/member/all/py")
        if response.status_code == 200:
            logging.info("모든 회원 정보가 성공적으로 가져와졌습니다.")
            return response.json()
        else:
            logging.error("회원 정보를 가져오는데 실패했습니다.")
            return []

    def get_all_member_address(self, member_id):
        """특정 회원의 주소 정보를 가져옵니다."""
        response = requests.get(self.http + f"/api/v1/member/address/{member_id}")
        if response.status_code == 200:
            return response.json()
        else:
            logging.error(f"회원 ID {member_id}의 주소 정보를 가져오는데 실패했습니다.")
            return []

    def select_datas(self, datas):
        """입력받은 데이터에서 무작위로 몇 개를 선택합니다."""
        if datas:
            selected_data = random.sample(datas, random.randint(1, len(datas)))
            logging.info(f"{len(selected_data)}개 데이터가 선택되었습니다.")
            return selected_data
        else:
            logging.warning("선택할 데이터가 없습니다.")
            return []

    def post_order(self):
        """선택된 상품과 회원 정보로 주문을 등록합니다."""
        for product in self.product_list:
            member = random.choice(self.member_list)
            member_address = self.get_all_member_address(member['id'])

            if not member_address:
                logging.warning(f"회원 ID {member['id']}의 주소 정보가 없어 주문을 스킵합니다.")
                continue

            member_address_id = random.choice(member_address)['id']
            order_data = {
                "memberId": member['id'],
                "memberAddressId": member_address_id,
                "cardName": random.choice(self.card_name),
                "cardNumber": fake.credit_card_number(),
                "orderList": [{"productId": product["id"], "count": random.randint(1, 20)}]
            }

            response = requests.post(self.http + "/api/v1/order", data=json.dumps(order_data), headers=self.headers)
            if response.status_code == 201:
                logging.info(f"주문 ID {response.json()['orderId']}가 성공적으로 생성되었습니다.")
            else:
                logging.error(f"주문 실패: {response.json()}")

    def main(self, run_env):
        """주문 등록 작업을 실행합니다."""
        logging.basicConfig(level=logging.INFO)
        logging.info("주문 등록 프로세스 시작...")

        self.setup_env(run_env)
        products = self.get_all_product()
        members = self.get_all_member()

        if products and members:
            self.product_list = self.select_datas(products)
            self.member_list = self.select_datas(members)
            self.post_order()
        logging.info("주문 등록 프로세스 완료.")


if __name__ == "__main__":
    order_app = RegisterOrder()
    order_app.main("dev")
