import logging
import os
import sys

import pyspark.sql.functions as F
import pyspark.sql.types as T
from pyspark.sql import DataFrame, Window

# 로깅 설정
logging.basicConfig(level=logging.INFO, format='%(asctime)s %(levelname)s %(message)s')
logger = logging.getLogger(__name__)
logger.addHandler(logging.StreamHandler(sys.stdout))

try:
    from etl_base import etl_base
except ImportError:
    from Common.etl_base import etl_base


class json_to_parquet(etl_base):

    def __init__(self):
        super().__init__()
        self.table_name = None
        self.order_col = None
        self.standard_col = None

        self.common_init()

        if self.run_env == "prod":
            self.prod_init()
        else:
            self.test_init()

    def common_init(self):
        self.standard_col: str = "cre_dtm"
        self.order_col: str = "etl_dtm"

    def prod_init(self):
        self.table_name: str = "bdp_wh.log_ingest"

    def test_init(self):
        self.table_name: str = "log_ingest"

    def read(self, path: str) -> DataFrame:
        schema = T.StructType([
            T.StructField("traceId", T.StringType(), True),
            T.StructField("clientIp", T.StringType(), True),
            T.StructField("time", T.StringType(), True),
            T.StructField("path", T.StringType(), True),
            T.StructField("method", T.StringType(), True),
            T.StructField("request", T.StringType(), True),
            T.StructField("response", T.StringType(), True),
            T.StructField("statusCode", T.StringType(), True),
            T.StructField("elapsedTimeMillis", T.IntegerType(), True)
        ])

        if self.run_env == "prod":
            json_path = os.path.join(path, self.base_dt.strftime("%Y-%m-%d"), '*/*.json')
        else:
            json_path = path

        logger.info(f"Reading JSON files from: {path}")
        return self.spark.read.json(json_path, schema=schema)

    def process(self, df: DataFrame) -> DataFrame:
        return (df
                .withColumn("etl_dtm", F.current_timestamp())
                .withColumn("cre_dtm", F.lit(self.base_dt.strftime("%Y-%m-%d")))
                )

    def write(self, df: DataFrame) -> None:
        standard_col = self.base_dt.strftime("%Y-%m-%d")

        logger.info(df.schema)

        # 디렉토리 존재 여부를 확인하고, 존재하는 경우 _deduplicate 메서드를 호출
        if self.spark._jsparkSession.catalog().tableExists(self.table_name):
            df_to_write = self._deduplicate(df)
        else:
            df_to_write = df

        df.show()

        df_to_write.write.partitionBy(self.standard_col).mode('overwrite').format("parquet").saveAsTable(
            self.table_name)

        logger.info("Success Write")

    def _deduplicate(self, df: DataFrame) -> DataFrame:
        try:
            # 해달 날짜의 데이터 만 들고 온다
            origin_df = (self.spark.sql(f"SELECT * FROM {self.table_name}")
                         .filter(F.col("cre_dtm") == F.lit(self.base_dt.strftime("%Y-%m-%d")))
                         )
            union_df = origin_df.unionAll(df.select(*origin_df.columns))
            window = Window.partitionBy('traceId').orderBy(F.col('etl_cre_dtm').desc())

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
    logger.info('ETL Job Started')
    path = "/data/ingestion/"
    try:
        json_to_parquet().run(path_or_table=path)
        logger.info('ETL Job Completed Successfully')
        sys.exit(0)
    except Exception as e:
        logger.error(f"ETL Job Failed: {e}")
        sys.exit(1)
