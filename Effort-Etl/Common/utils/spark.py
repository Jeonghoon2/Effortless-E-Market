import os
from datetime import datetime
from typing import Optional

from pyspark.sql import SparkSession


def create_session(app_name: Optional[str] = None, local: bool = False):
    os.environ["SPARK_LOCAL_IP"] = "127.0.0.1"
    spark = SparkSession.getActiveSession()
    if spark:
        return spark

    app_name = app_name or f"spark-app-{int(datetime.now().timestamp())}"

    builder = SparkSession.builder.appName(app_name)

    if local:
        builder = (builder.master("local")
                   .config("spark.driver.host", "127.0.0.1")
                   .config("spark.driver.bindAddress", "127.0.0.1")
                   )

    return builder.enableHiveSupport().getOrCreate()



