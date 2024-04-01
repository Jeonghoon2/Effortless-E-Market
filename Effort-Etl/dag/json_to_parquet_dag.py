import os
from datetime import timedelta, datetime
from airflow import DAG
from operators.spark import PysparkClusterOperator, SparkJobResource

default_args = {
    'owner': 'altera',
    'retries': 0,
    'retry_delay': timedelta(minutes=2),
    'execution_timeout': timedelta(minutes=120)
}

DAG_ID = os.path.basename(__file__).replace(".pyc", "").replace(".py", "")

with DAG(
        dag_id=DAG_ID,
        default_args=default_args,
        schedule_interval='00 15 * * *',
        catchup=False,
        start_date=datetime(2024, 1, 20),
        dagrun_timeout=timedelta(hours=8),
        max_active_runs=3,
        concurrency=1,
        tags=['ingestion', 'json', 'parquet']
) as dag:
    airflow_root = "/usr/bdp/airflow/dags"
    execution_date = "{{ execution_date.in_timezone('Asia/Seoul').strftime('%Y-%m-%d') }}"
    json_to_parquet_task = PysparkClusterOperator(
        task_id="json_to_parquet",
        source_path=f"{airflow_root}/spark/json_to_parquet.py",
        job_resource=SparkJobResource(
            driver_core=1,
            driver_mem="2G",
            executor_num=2,
            executor_core=1,
            executor_mem="2G"
        ),
        additional_arguments=[
            "--base_dt", execution_date,
            "--run_env", "prod"
        ],
        py_files=[
            f"{airflow_root}/spark/utils/spark.py",
            f"{airflow_root}/spark/utils/etl_base.py"
        ],
        hadoop_user_name="hive",
        python_env_name="bdp-default",
        python_env_version="latest",
        dag=dag
    )

    json_to_parquet_task
