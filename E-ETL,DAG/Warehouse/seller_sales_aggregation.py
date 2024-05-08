import logging
import sys
import pyspark.sql.types as T
import pyspark.sql.functions as F
from pyspark.sql import DataFrame, Window

# Logging setup
logging.basicConfig(level=logging.INFO, format='%(asctime)s %(levelname)s - %(message)s')
logger = logging.getLogger(__name__)
logger.addHandler(logging.StreamHandler(sys.stdout))

try:
    from etl_base import etl_base
    from write_to_jdbc import JdbcBuilder, write_to_jdbc
except ImportError:
    from Common.etl_base import etl_base
    from Common.utils.write_to_jdbc import JdbcBuilder, write_to_jdbc


class SellerSalesAggregation(etl_base):
    READ_TABLE_PROD = "bdp_wh.path"
    READ_TABLE_LOCAL = "path"
    WRITE_TABLE_PROD = "bdp_wh.seller_sales_agg"
    WRITE_TABLE_LOCAL = "seller_sales_agg"
    JDBC_TABLE = "seller_sales_agg"

    def __init__(self):
        super().__init__()

        self.read_table = None
        self.write_table = None
        self.partitionList = ["cre_dtm", "seller_id", "product_id"]

    def read(self) -> DataFrame:
        self.read_table = self.READ_TABLE_PROD if self.run_env == "prod" else self.READ_TABLE_LOCAL
        df = self.spark.sql(f"""
            SELECT *
            FROM {self.read_table}
            WHERE method='POST' 
            AND path='_api_v1_order' 
            AND cre_dtm = '{self.base_dt.strftime('%Y-%m-%d')}'
            """)
        if df.rdd.isEmpty():
            logger.error("DataFrame이 비어 있습니다.")
            sys.exit(405)
        else:
            logger.info(f"성공적으로 {self.read_table}의 테이블에서 데이터를 읽어 들였습니다.")
            return df

    def process(self, df: DataFrame) -> DataFrame:
        response_schema = T.StructType([
            T.StructField("orderDetailList", T.ArrayType(T.StructType([
                T.StructField("sellerId", T.IntegerType()),
                T.StructField("productId", T.IntegerType()),
                T.StructField("productName", T.StringType()),
                T.StructField("count", T.IntegerType()),
                T.StructField("totalPrice", T.IntegerType()),
            ]))),
            T.StructField("createAt", T.StringType())
        ])

        df = df.withColumn("response_parsed", F.from_json(F.col("response"), response_schema))
        df = df.withColumn("orderDetails", F.explode("response_parsed.orderDetailList"))
        df = df.withColumn("etl_dtm", F.current_timestamp())
        df = df.select(
            F.col("traceId"),
            F.col("orderDetails.sellerId").alias("seller_id"),
            F.col("orderDetails.productId").alias("product_id"),
            F.col("orderDetails.productName").alias("product_name"),
            F.col("orderDetails.count").alias("total_sales"),
            F.col("orderDetails.totalPrice").alias("total_revenue"),
            F.col("response_parsed.createAt").alias("create_at"),
            "etl_dtm",
            "cre_dtm")

        df.show(truncate=True)

        return df

    def write(self, df: DataFrame):
        self.write_table = self.WRITE_TABLE_PROD if self.run_env == "prod" else self.WRITE_TABLE_LOCAL

        logger.info("중복 제거 전")
        df.show(truncate=False)
        try:
            df_to_write = self._deduplicate(df) if self.table_exists(self.write_table) else df

            logger.info("중복 제거 후")
            df_to_write.show(truncate=False)

            df_to_write.write \
                .partitionBy(self.partitionList) \
                .mode("overwrite") \
                .format('parquet') \
                .saveAsTable(self.write_table)

            logger.info("성공적으로 적으로 DataFrame을 저장하였습니다.")

            jdbc_builder = JdbcBuilder() \
                .url(self.jdbc_url) \
                .username(self.jdbc_username) \
                .password(self.jdbc_password) \
                .database_name(self.jdbc_database) \
                .db_table(self.JDBC_TABLE)

            jdbc_env = jdbc_builder.build()

            logger.info("Jdbc Connection Info")

            logger.info(jdbc_builder.to_print())
            write_to_jdbc(df_to_write, jdbc_env)
        except Exception as e:
            logger.error(f"데이터 저장에 실패하였습니다. Error: {str(e)}")
            sys.exit(407)

    def _deduplicate(self, df: DataFrame) -> DataFrame:
        try:
            origin_df = self.spark.sql(f"SELECT * FROM {self.write_table}")

            union_df = origin_df.unionAll(df.select(*origin_df.columns))

            window = Window.partitionBy("traceId").orderBy(F.col('etl_dtm').desc())

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
    logger.info('ETL name : Seller Sales Aggregation')
    try:
        SellerSalesAggregation().run()
        logger.info('ETL Job Completed Successfully')
        logger.info('')
        logger.info('================================================')
        sys.exit(0)
    except Exception as e:
        logger.error(f"ETL Job Failed: {e}")
        logger.info('')
        logger.info('================================================')
        sys.exit(1)
