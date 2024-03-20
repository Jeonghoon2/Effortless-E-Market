import logging
import sys
import pyspark.sql.functions as F
from pyspark.sql import DataFrame, Window
from pyspark.sql.utils import AnalysisException

logging.basicConfig(level=logging.INFO, format='%(asctime)s %(levelname)s %(message)s')
logger = logging.getLogger(__name__)
logger.addHandler(logging.StreamHandler(sys.stdout))

try:
    from etl_base import etl_base
except ImportError:
    from Common.etl_base import etl_base


class classification_by_route(etl_base):

    def __init__(self):
        super().__init__()
        self.run_env = None
        self.table = None  # Read Table
        self.write_table = None

        if self.run_env == "prod":
            self.table: str = "bdp_wh.log_ingest"
            self.write_table = "bdp_wh.path"
        else:
            self.table = "log_ingest"
            self.write_table = "path"

    def read(self, table: str) -> DataFrame:
        try:
            return self.spark.sql(f"SELECT * FROM {table}")
        except AnalysisException as e:
            raise ValueError(f"Failed to read from table {table}. Error: {str(e)}")

    def process(self, df: DataFrame) -> DataFrame:
        try:
            return (
                df
                .drop("clientIp", "elapsedTimeMillis", "etl_hh", "etl_cre_dtm")
                .withColumn("etl_cre_dtm", F.current_timestamp())
                .withColumn("path", F.regexp_replace("path", "/", "_"))
            )
        except Exception as e:
            raise RuntimeError(f"Failed to process DataFrame. Error: {str(e)}")

    def write(self, df: DataFrame) -> None:
        try:
            if self.spark._jsparkSession.catalog().tableExists(self.write_table):
                df_to_write = self._deduplicate(df)
            else:
                df_to_write = df

            (df_to_write.write
             .partitionBy("method", "path", "cre_dtm")
             .mode("overwrite")
             .format('parquet')
             .saveAsTable(self.write_table)
             )

        except Exception as e:
            raise RuntimeError(f"Failed to write DataFrame to table {self.write_table}. Error: {str(e)}")

    def _deduplicate(self, df: DataFrame) -> DataFrame:
        try:
            origin_df = self.spark.sql(f"SELECT * FROM {self.write_table}")
            union_df = origin_df.unionAll(df.select(*origin_df.columns))
            window = Window.partitionBy('traceId').orderBy(F.col('etl_cre_dtm').desc())

            deduplicated_df = (union_df.withColumn('row_no', F.row_number().over(window))
                               .filter(F.col("row_no") == 1).drop('row_no')
                               .localCheckpoint())

            return deduplicated_df
        except Exception as e:
            raise RuntimeError(f"Failed to deduplicate DataFrame. Error: {str(e)}")


if __name__ == "__main__":
    table = 'log_ingest'
    classification_by_route().run(path_or_table=table)
