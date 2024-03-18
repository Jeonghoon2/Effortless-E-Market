import argparse
import sys
from abc import ABCMeta, abstractmethod
from datetime import datetime, timedelta
from typing import NoReturn, Dict, Any

from pyspark.sql import DataFrame

try:
    from spark import create_session
except:
    from Common.utils.spark import create_session


class etl_base(metaclass=ABCMeta):

    def __init__(self):
        self.spark = create_session()
        self.parser: argparse.ArgumentParser = argparse.ArgumentParser()
        self.parser.add_argument('--base_dt', type=lambda s: datetime.strptime(s, '%Y-%m-%d'), required=False)
        self.args = {}
        self.base_dt: datetime = None
        self.prev_dt: datetime = None
        self.next_dt: datetime = None
        self.run_env: str

    def args_define(self) -> NoReturn:
        ...

    @abstractmethod
    def read(self, path_or_table: str) -> Dict[str, Any] | DataFrame:
        ...

    @abstractmethod
    def process(self, args: Dict[str, Any] | DataFrame) -> Dict[str, Any] | DataFrame:
        ...

    @abstractmethod
    def write(self, args: Dict[str, Any] | DataFrame) -> NoReturn:
        ...

    def run(self, **kwargs):
        self.args = self.parser.parse_args().__dict__
        self.base_dt = self.args['base_dt']
        self.run_env = self.args['run_env']
        self.prev_dt = self.base_dt - timedelta(days=1)
        self.next_dt = self.base_dt + timedelta(days=1)

        df = self.read(kwargs['path_or_table'])
        if df == None:
            return sys.exit(1)
        trans_df = self.process(df)
        self.write(trans_df)
