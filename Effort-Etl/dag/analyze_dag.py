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

etl_script_list = [
    # 해당 일을 기준으로 조회수 가 높은 상품을 분석
    ('highest_number_of_views', 'highest_number_of_views.py'),
    # 해당 일을 기준으로 판매량이 가장 높은 상품을 분석
    ('sales_rank', 'sales_rank.py'),
    # 해당 일을 기준으로 년, 월, 일, 시간, 분을 기준으로 판매자의 판매량, 수익을 분석
    ('seller_sales_aggregation', 'seller_sales_aggregation.py'),
    # 해당 일을 기준으로 판매자가 등록한 상품의 카테고리 별 판매 순위 분석
    ('category_by_sales', 'category_by_sales.py'),
]

with DAG(
        dag_id=DAG_ID,
        default_args=default_args,
        schedule_interval='6 15 * * *',
        catchup=False,
        start_date=datetime(2024, 1, 20),
        dagrun_timeout=timedelta(hours=8),
        max_active_runs=3,
        concurrency=1,
        tags=['analysis', 'etl']
) as dag:

    # ExternalTaskSensor
    wait_for_classification_by_route_etl = ExternalTaskSensor(
        task_id='wait_for_classification_by_route_etl',
        external_dag_id='classification_by_route_dag',
        external_task_id='classification_by_route_task',
        execution_delta=timedelta(minutes=5),
        allowed_states=['success'],
        timeout=timedelta(minutes=60),
        poke_interval=timedelta(minutes=1),
        mode='poke',
    )

    airflow_root = "/usr/bdp/airflow/dags"
    execution_date = "{{ execution_date.in_timezone('Asia/Seoul').strftime('%Y-%m-%d') }}"
    etl_tasks = []

    for task_id, script_path in etl_script_list:
        etl_task = PysparkClusterOperator(
            task_id=task_id,
            source_path=os.path.join(airflow_root, 'spark', script_path),
            pool='analyze_pool',
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

        etl_tasks.append(etl_task)

    wait_for_classification_by_route_etl >> etl_tasks
