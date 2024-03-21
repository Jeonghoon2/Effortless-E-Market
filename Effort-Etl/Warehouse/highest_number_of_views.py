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

            return product_list

        except AnalysisException as e:
            raise ValueError(f"Failed to read from table {self.read_table}. Error: {str(e)}")

    def process(self, df: DataFrame) -> DataFrame:
        return (
            df
            .groupBy("product_id", "cre_dtm", "etl_cre_dtm")
            .count()
            .orderBy(F.col("count").desc())
        )

    def write(self, df: DataFrame) -> None:

        # 디렉토리 존재 여부를 확인하고, 존재하는 경우 _deduplicate 메서드를 호출
        if self.spark._jsparkSession.catalog().tableExists(self.write_table):
            df_to_write = self._deduplicate(df)
        else:
            df_to_write = df

        df.show()

        df_to_write.write.partitionBy(self.partitionList) \
            .mode('overwrite') \
            .format("parquet") \
            .saveAsTable(self.write_table)

        logger.info("====== Success Write ======")

    def _deduplicate(self, df: DataFrame) -> DataFrame:
        try:

            origin_df = (self.spark.sql(f"SELECT * FROM {self.write_table}")
                         .filter(F.col("cre_dtm") == self.base_dt)
                         )
            union_df = origin_df.unionAll(df.select(*origin_df.columns))
            window = Window.partitionBy('product_id').orderBy(F.col('etl_cre_dtm').desc())

            deduplicated_df = (union_df.withColumn('row_no', F.row_number().over(window))
                               .filter(F.col("row_no") == 1).drop('row_no')
                               .localCheckpoint())

            return deduplicated_df
        except Exception as e:
            raise RuntimeError(f"Failed to deduplicate DataFrame. Error: {str(e)}")

    def _path_exists(self, path: str) -> bool:
        hadoop_conf = self.spark._jsc.hadoopConfiguration()
        hadoop_fs = self.spark._jvm.org.apache.hadoop.fs.FileSystem.get(hadoop_conf)
        return hadoop_fs.exists(self.spark._jvm.org.apache.hadoop.fs.Path(path))


if __name__ == "__main__":
    try:
        highest_number_of_views().run()
        logger.info('ETL Job Completed Successfully')
        sys.exit(0)
    except Exception as e:
        logger.error(f"ETL Job Failed: {e}")
        sys.exit(1)
