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
    WRITE_TABLE_PROD = "bdp_wh.log_ingest"
    WRITE_TABLE_LOCAL = "log_ingest"
    READ_PATH_PROD = "/data/ingestion/"
    READ_PATH_LOCAL = "../data/JsonToParquet-A.json"

    def __init__(self):
        super().__init__()
        self.read_path = self.READ_PATH_PROD if self.run_env == "prod" else self.READ_PATH_LOCAL
        self.write_table = self.WRITE_TABLE_PROD if self.run_env == "prod" else self.WRITE_TABLE_LOCAL
        self.partitionList = ["cre_dtm"]

    def read(self) -> DataFrame:
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

        json_path = self.get_json_path()

        df = self.spark.read.schema(schema).json(json_path)

        if df.rdd.isEmpty():
            logger.error("DataFrame이 비어 있습니다.")
            sys.exit(405)
        else:
            logger.info("성공적으로 Json 파일을 읽어 들였습니다.")
            return df

    def process(self, df: DataFrame) -> DataFrame:
        try:
            df = (df
                  .withColumn("etl_dtm", F.current_timestamp())
                  .withColumn("cre_dtm", F.lit(self.base_dt.strftime("%Y-%m-%d")))
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
            logger.error(f"데이터 저장에 실패하였습니다. Error: {e}")
            sys.exit(407)

    def _deduplicate(self, df: DataFrame) -> DataFrame:
        try:
            origin_df = self.spark.sql(f"""
            SELECT * 
            FROM {self.write_table}
            WHERE cre_dtm = '{self.base_dt}'
            """)

            union_df = origin_df.unionAll(df.select(*origin_df.columns))
            window = Window.partitionBy('traceId').orderBy(F.col('etl_cre_dtm').desc())

            deduplicated_df = (union_df.withColumn('row_no', F.row_number().over(window))
                               .filter(F.col("row_no") == 1).drop('row_no')
                               .localCheckpoint())

            return deduplicated_df
        except Exception as e:
            logger.error(f"DataFrame 중복 제거를 실패했습니다. Error: {str(e)}")
            sys.exit(408)

    # 테이블 유무 체크
    def table_exists(self, write_table):
        return self.spark._jsparkSession.catalog().tableExists(write_table)

    # Json File 유무 체크
    def _path_exists(self, path: str) -> bool:
        hadoop_conf = self.spark._jsc.hadoopConfiguration()
        hadoop_fs = self.spark._jvm.org.apache.hadoop.fs.FileSystem.get(hadoop_conf)
        return hadoop_fs.exists(self.spark._jvm.org.apache.hadoop.fs.Path(path))

    # Json 파일 가져오기
    def get_json_path(self):
        if self.run_env == "prod":
            json_path = os.path.join(self.read_path, self.base_dt.strftime("%Y-%m-%d"), '*/*.json')
            if not self._path_exists(json_path):
                logger.error("Json File을 찾을 수 없습니다.")
                sys.exit(404)
            return json_path
        else:
            return self.read_path


if __name__ == "__main__":
    logger.info('================================================')
    logger.info('')
    logger.info('ETL Job Started')
    logger.info('ETL Info')
    logger.info('ETL name : Json To Parquet')
    try:
        json_to_parquet().run()
        logger.info('ETL Job Completed Successfully')
        logger.info('')
        logger.info('================================================')
        sys.exit(0)
    except Exception as e:
        logger.error(f"ETL Job Failed: {e}")
        logger.info('')
        logger.info('================================================')
        sys.exit(1)
