from pyspark.sql import DataFrame


class JdbcEnv:
    def __init__(self):
        self.url: str = None
        self.username: str = None
        self.password: str = None
        self.database_name: str = None
        self.db_table: str = None
        self.batch_size: str = "1000"
        self.mode: str = "append"


class JdbcBuilder:
    def __init__(self):
        self.jdbc = JdbcEnv()

    def url(self, url):
        self.jdbc.url = url
        return self

    def username(self, username):
        self.jdbc.username = username
        return self

    def password(self, password):
        self.jdbc.password = password
        return self

    def database_name(self, database_name):
        self.jdbc.database_name = database_name
        return self

    def db_table(self, database_table):
        self.jdbc.db_table = database_table
        return self

    def batch_size(self, batch_size):
        self.jdbc.batch_size = batch_size
        return self

    def mode(self, mode):
        self.jdbc.mode = mode
        return self

    def build(self):

        missing_list = []
        if not self.jdbc.url:
            missing_list.append("url")

        if not self.jdbc.username:
            missing_list.append("username")

        if not self.jdbc.password:
            missing_list.append("password")

        if not self.jdbc.database_name:
            missing_list.append("database_name")

        if not self.jdbc.db_table:
            missing_list.append("db_table")

        if missing_list:
            raise ValueError(f"""
            JDBC 설정에서 누락된 Feild가 있습니다. \n
            Fields : {', '.join(missing_list)}
            """)

        return self.jdbc


def write_to_jdbc(df: DataFrame, jdbc: JdbcEnv):
    df.write \
        .format("jdbc") \
        .option("url", jdbc.url) \
        .option("user", jdbc.username) \
        .option("password", jdbc.password) \
        .option("dbtable", jdbc.db_table) \
        .option("batchsize", jdbc.batch_size) \
        .mode(jdbc.mode) \
        .save()
