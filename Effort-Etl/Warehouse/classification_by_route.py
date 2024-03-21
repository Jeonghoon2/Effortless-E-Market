import logging
import re
import sys

import pyspark.sql.functions as F
from pyspark.sql import DataFrame, Window
from pyspark.sql.types import StringType

logging.basicConfig(level=logging.INFO, format='%(asctime)s %(levelname)s %(message)s')
logger = logging.getLogger(__name__)
logger.addHandler(logging.StreamHandler(sys.stdout))

try:
    from etl_base import etl_base
except ImportError:
    from Common.etl_base import etl_base


class classification_by_route(etl_base):
    READ_TABLE_PROD = "bdp_wh.log_ingest"
    READ_TABLE_LOCAL = "log_ingest"
    WRITE_TABLE_PROD = "bdp_wh.path"
    WRITE_TABLE_LOCAL = "path"

    def __init__(self):
        super().__init__()
        self.path_udf = F.udf(self.path_transform, StringType())
        self.read_table = self.READ_TABLE_PROD if self.run_env == "prod" else self.READ_TABLE_LOCAL
        self.write_table = self.WRITE_TABLE_PROD if self.run_env == "prod" else self.WRITE_TABLE_LOCAL
        self.partitionList = ["method", "path", "cre_dtm"]

    def read(self) -> DataFrame:
        try:

            df = self.spark.sql(f"SELECT * FROM {self.read_table}")

            if df.rdd.isEmpty():
                logger.error("DataFrame이 비어 있습니다.")
                sys.exit(404)
            else:
                logger.info(f"성공적으로 {self.read_table}의 테이블에서 데이터를 읽어 들였습니다.")
                return df

        except Exception as e:
            logger.error(f"{self.read_table} 테이블을 읽어 들이지 못했습니다. Error: {str(e)}")
            sys.exit(400)

    def process(self, df: DataFrame) -> DataFrame:
        try:
            df = (
                df
                .drop("clientIp", "elapsedTimeMillis", "etl_hh", "etl_cre_dtm")
                .withColumn("etl_cre_dtm", F.current_timestamp())
                .withColumn("path", F.regexp_replace("path", "/", "_"))
            )
            return df.withColumn("path", self.path_udf(F.col("path")))

        except Exception as e:
            raise RuntimeError(f"DataFrame 처리 하는 도중 오류가 발생했습니다. Error: {str(e)}")

    def write(self, df: DataFrame) -> None:
        try:
            df_to_write = self._deduplicate(df) if self.table_exists(self.write_table) else df

            df_to_write.write \
                .partitionBy(self.partitionList) \
                .mode("overwrite") \
                .format('parquet') \
                .saveAsTable(self.write_table)

            logger.info("성공적으로 적으로 DataFrame을 저장하였습니다.")
        except Exception as e:
            raise RuntimeError(f"데이터 저장에 실패하였습니다. Error: {str(e)}")

    def _deduplicate(self, df: DataFrame) -> DataFrame:
        try:
            origin_df = self.spark.sql(f"SELECT * FROM {self.write_table}") \
                .filter(F.col("cre_dtm") == self.base_dt)
            union_df = origin_df.unionAll(df.select(*origin_df.columns))
            window = Window.partitionBy('traceId').orderBy(F.col('etl_cre_dtm').desc())

            deduplicated_df = (union_df.withColumn('row_no', F.row_number().over(window))
                               .filter(F.col("row_no") == 1).drop('row_no')
                               .localCheckpoint())

            return deduplicated_df
        except Exception as e:
            raise RuntimeError(f"DataFrame 중복 제거를 실패했습니다. Error: {str(e)}")

        # 테이블 유무 체크

    def table_exists(self, write_table):
        return self.spark._jsparkSession.catalog().tableExists(write_table)

    @staticmethod
    def path_transform(path):
        path = path.replace('/', '_')
        path = re.sub(r'_\d+', '_(id)', path)
        return path


if __name__ == "__main__":
    logger.info('================================================')
    logger.info('')
    logger.info('ETL Job Started')
    logger.info('ETL Info')
    logger.info('ETL name : Classification By Route')
    try:
        classification_by_route().run()
        logger.info('ETL Job Completed Successfully')
        logger.info('')
        logger.info('================================================')
        sys.exit(0)
    except Exception as e:
        logger.error(f"ETL Job Failed: {e}")
        logger.info('')
        logger.info('================================================')
        sys.exit(1)
