import os
from datetime import timedelta, datetime
from airflow import DAG
from airflow.sensors.external_task_sensor import ExternalTaskSensor
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
        schedule_interval='5 15 * * *',
        catchup=False,
        start_date=datetime(2024, 1, 20),
        dagrun_timeout=timedelta(hours=8),
        max_active_runs=3,
        concurrency=1,
        tags=['classification', 'path']
) as dag:

    # ExternalTaskSensor
    wait_for_json_to_parquet_etl = ExternalTaskSensor(
        task_id='wait_for_json_to_parquet_etl',
        external_dag_id='json_to_cleansing_dag',
        external_task_id='json_to_cleansing',
        execution_delta=timedelta(minutes=5),
        allowed_states=['success'],
        timeout=timedelta(minutes=60),
        poke_interval=timedelta(minutes=1),
        mode='poke',
    )

    airflow_root = "/usr/bdp/airflow/dags"
    execution_date = "{{ execution_date.in_timezone('Asia/Seoul').strftime('%Y-%m-%d') }}"

    classification_by_route_task = PysparkClusterOperator(
        task_id="classification_by_route_task",
        source_path=f"{airflow_root}/spark/classification_by_route.py",
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
    )

    wait_for_json_to_parquet_etl >> classification_by_route_task
