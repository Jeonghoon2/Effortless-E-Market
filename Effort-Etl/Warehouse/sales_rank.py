import logging
import sys

import pyspark.sql.functions as F
from pyspark.sql import DataFrame, Window
import pyspark.sql.types as T

# 로깅 설정
logging.basicConfig(level=logging.INFO, format='%(asctime)s %(levelname)s %(message)s')
logger = logging.getLogger(__name__)
logger.addHandler(logging.StreamHandler(sys.stdout))

try:
    from etl_base import etl_base
except ImportError:
    from Common.etl_base import etl_base


class sales_rank(etl_base):
    READ_TABLE_PROD = "bdp_wh.path"
    READ_TABLE_LOCAL = "path"
    WRITE_TABLE_PROD = "bdp_wh.highest_sales_rank"
    WRITE_TABLE_LOCAL = "highest_sales_rank"

    def __init__(self):
        super().__init__()

        self.read_table = None
        self.write_table = None
        self.partitionList = ["cre_dtm"]

    def read(self) -> DataFrame:

        self.read_table = self.READ_TABLE_PROD if self.run_env == "prod" else self.READ_TABLE_LOCAL

        try:
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
                df.show(truncate=False)
                return df

        except Exception as e:
            logger.error(f"{self.read_table} 테이블을 찾을 수 없거나 예기치 못한 오류로 인하여 데이터를 불러오지 못하였습니다."
                         f" Error: {str(e)}")
            sys.exit(404)

    def process(self, df: DataFrame) -> DataFrame:
        try:
            response_schema = T.StructType([
                T.StructField("orderId", T.IntegerType()),
                T.StructField("memberId", T.IntegerType()),
                T.StructField("recipientAddress", T.StringType()),
                T.StructField("createAt", T.StringType()),
                T.StructField("etl_dtm", T.DateType()),
                T.StructField("cre_dtm", T.DateType()),
                T.StructField("orderDetailList", T.ArrayType(T.StructType([
                    T.StructField("sellerId", T.IntegerType()),
                    T.StructField("productId", T.IntegerType()),
                    T.StructField("productName", T.StringType()),
                    T.StructField("categoryId", T.IntegerType()),
                    T.StructField("categoryName", T.StringType()),
                    T.StructField("count", T.IntegerType()),
                    T.StructField("totalPrice", T.IntegerType()),
                ]))),
            ])

            # response 필드 내의 JSON 문자열을 구조화된 데이터로 변환
            df = df.withColumn("response_parsed", F.from_json(F.col("response"), response_schema))

            # 정확히 orderDetailList 필드를 explode 함수로 처리
            df = df.withColumn("orderDetail", F.explode(F.col("response_parsed.orderDetailList")))

            df = df.select(
                F.col("orderDetail.productId").alias("product_id"),
                F.col("orderDetail.productName").alias("product_name"),
                F.col("orderDetail.categoryId").alias("category_id"),
                F.col("orderDetail.categoryName").alias("category_name"),
                "cre_dtm",
                "etl_dtm",
                F.col("orderDetail.count").cast("integer").alias("count"),
                F.col("orderDetail.totalPrice").cast("integer").alias("totalPrice")
            )

            sales_data = df.groupBy("product_id",
                                    "product_name",
                                    "category_id",
                                    "category_name",
                                    "cre_dtm",
                                    "etl_dtm") \
                .agg(
                F.sum("count").alias("total_sales"),
                F.sum("totalPrice").alias("total_revenue")
            ) \
                .orderBy(F.col("total_sales").desc(), F.col("total_revenue").desc())

            sales_data = sales_data.withColumn("etl_dtm", F.current_timestamp())
            sales_data.show()
            return sales_data

        except Exception as e:
            logger.error(f"DataFrame 처리 하는 도중 오류가 발생했습니다. Error: {str(e)}")
            sys.exit(406)

    def write(self, df: DataFrame) -> None:

        self.write_table = self.WRITE_TABLE_PROD if self.run_env == "prod" else self.WRITE_TABLE_LOCAL

        try:
            df_to_write = self._deduplicate(df) if self.table_exists(self.write_table) else df

            df_to_write.write \
                .partitionBy(self.partitionList) \
                .mode("overwrite") \
                .format('parquet') \
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

            window = Window.partitionBy('product_id').orderBy(F.col('etl_dtm').desc())

            deduplicated_df = (union_df.withColumn('row_no', F.row_number().over(window))
                               .filter(F.col("row_no") == 1).drop('row_no')
                               .localCheckpoint())

            return deduplicated_df
        except Exception as e:
            logger.error(f"DataFrame 중복 제거를 실패했습니다. Error: {str(e)}")
            sys.exit(408)

    def table_exists(self, write_table):
        return self.spark._jsparkSession.catalog().tableExists(write_table)


if __name__ == "__main__":
    try:
        sales_rank().run()
        logger.info('ETL Job Completed Successfully')
        sys.exit(0)
    except Exception as e:
        logger.error(f"ETL Job Failed: {e}")
        sys.exit(1)
