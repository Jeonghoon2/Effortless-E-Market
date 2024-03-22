import logging
import sys

import pyspark.sql.functions as F
from pyspark.sql import DataFrame, Window
from pyspark.sql.utils import AnalysisException

# 로깅 설정
logging.basicConfig(level=logging.INFO, format='%(asctime)s %(levelname)s %(message)s')
logger = logging.getLogger(__name__)
logger.addHandler(logging.StreamHandler(sys.stdout))

try:
    from etl_base import etl_base
except ImportError:
    from Common.etl_base import etl_base


class highest_number_of_views(etl_base):
    READ_TABLE_PROD = "bdp_wh.path"
    READ_TABLE_LOCAL = "path"
    WRITE_TABLE_PROD = "bdp_wh.top_views"
    WRITE_TABLE_LOCAL = "top_views"

    def __init__(self):
        super().__init__()

        self.read_table = self.READ_TABLE_PROD if self.run_env == "prod" else self.READ_TABLE_LOCAL
        self.write_table = self.WRITE_TABLE_PROD if self.run_env == "prod" else self.WRITE_TABLE_LOCAL
        self.partitionList = ["cre_dtm"]

    def read(self) -> DataFrame:

        try:
            product_list = self.spark.sql(f"""
            SELECT *, get_json_object(response, '$.id') as product_id
            FROM {self.read_table}
            WHERE cre_dtm='{self.base_dt}'
            AND path='_api_v1_product_(id)'
            """)

            if product_list.rdd.isEmpty():
                logger.error("DataFrame이 비어 있습니다.")
                sys.exit(404)
            else:
                logger.info(f"성공적으로 {self.read_table}의 테이블에서 데이터를 읽어 들였습니다.")
                return product_list

        except Exception as e:
            logger.error(f"{self.read_table} 테이블을 찾을 수 없거나 예기치 못한 오류로 인하여 데이터를 불러오지 못하였습니다."
                         f" Error: {str(e)}")
            sys.exit(404)

    def process(self, df: DataFrame) -> DataFrame:
        try:
            df = (
                df
                .groupBy("product_id", "cre_dtm", "etl_cre_dtm")
                .count()
                .orderBy(F.col("count").desc())
            )

            return df
        except Exception as e:
            logger.error(f"DataFrame 처리 하는 도중 오류가 발생했습니다. Error: {str(e)}")
            sys.exit(406)

    def write(self, df: DataFrame) -> None:
        try:
            df_to_write = self._deduplicate(df) if self.table_exists(self.write_table) else df

            df.show()

            df_to_write.write \
                .partitionBy(self.partitionList) \
                .mode('overwrite') \
                .format("parquet") \
                .saveAsTable(self.write_table)

            logger.info("성공적으로 적으로 DataFrame을 저장하였습니다.")
        except Exception as e:
            logger.error(f"데이터 저장에 실패하였습니다. Error: {str(e)}")
            sys.exit(407)

    def _deduplicate(self, df: DataFrame) -> DataFrame:
        try:
            origin_df = self.spark.sql(f"SELECT * FROM {self.write_table}") \
                .filter(F.col("cre_dtm") == self.base_dt)

            union_df = origin_df.unionAll(df.select(*origin_df.columns))
            window = Window.partitionBy('product_id').orderBy(F.col('etl_cre_dtm').desc())

            deduplicated_df = (union_df.withColumn('row_no', F.row_number().over(window))
                               .filter(F.col("row_no") == 1).drop('row_no')
                               .localCheckpoint())

            return deduplicated_df
        except Exception as e:
            logger.error(f"DataFrame 중복 제거를 실패했습니다. Error: {str(e)}")
            sys.exit(408)

    def _path_exists(self, path: str) -> bool:
        hadoop_conf = self.spark._jsc.hadoopConfiguration()
        hadoop_fs = self.spark._jvm.org.apache.hadoop.fs.FileSystem.get(hadoop_conf)
        return hadoop_fs.exists(self.spark._jvm.org.apache.hadoop.fs.Path(path))

    def table_exists(self, write_table):
        return self.spark._jsparkSession.catalog().tableExists(write_table)


if __name__ == "__main__":
    logger.info('================================================')
    logger.info('')
    logger.info('ETL Job Started')
    logger.info('ETL Info')
    logger.info('ETL name : Highest Number Of Views')
    try:
        highest_number_of_views().run()
        logger.info('ETL Job Completed Successfully')
        logger.info('')
        logger.info('================================================')
        sys.exit(0)
    except Exception as e:
        logger.error(f"ETL Job Failed: {e}")
        logger.info('')
        logger.info('================================================')
        sys.exit(1)
