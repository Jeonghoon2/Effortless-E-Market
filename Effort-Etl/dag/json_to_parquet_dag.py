from datetime import datetime, timedelta

from airflow import DAG
from airflow.operators.bash import BashOperator

with DAG(
        'json_to_parquet_dag',
        default_args={
            'owner': 'airflow',
            'start_date': datetime(2024, 1, 14)
        },
        schedule_interval=timedelta(days=1),
        catchup=False,
) as dag:
    bash_task = BashOperator(
        task_id='jtp_task',
        bash_command='/usr/bdp/airflow/dags/airflow-de/jtp_app_submit.sh client {{ ds }} prod',
        env={
            'HADOOP_CONF_DIR': '/usr/bdp/hadoop/etc/hadoop',
            'SPARK_HOME': '/usr/bdp/spark',
            'PYSPARK_PYTHON': '/usr/bin/python'
        },
        dag=dag,
    )

    bash_task
