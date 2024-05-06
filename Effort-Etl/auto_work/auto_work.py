import argparse
import random
import time

from get_product import GetProduct
from register_order import RegisterOrder
from register_product import RegisterProduct
from register_seller import RegisterSeller
from register_user import RegisterUser


class AutoWork:

    def __init__(self):
        self.parser: argparse.ArgumentParser = argparse.ArgumentParser()
        self.parser.add_argument('--run_env', required=True)
        self.args = {}

    def auto_work(self):
        self.args = self.parser.parse_args().__dict__
        run_env = self.args.get('run_env')

        """ 사용자 등록 """
        for _ in range(0, random.randint(1, 5)):
            RegisterUser().main(run_env)
            time.sleep(0.5)

        """ 판매자 등록 """
        for _ in range(0, random.randint(1, 3)):
            RegisterSeller().main(run_env)
            time.sleep(0.5)

        """ 상품 등록 """
        for _ in range(0, random.randint(1, 5)):
            RegisterProduct().main(run_env)
            time.sleep(0.3)

        """ 상품 조회 """
        GetProduct().main(run_env)

        """ 상품 구매 """
        RegisterOrder().main(run_env)


if __name__ == '__main__':
    auto_work = AutoWork()
    auto_work.auto_work()
