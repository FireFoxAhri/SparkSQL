import org.apache.spark.sql.SparkSession

object Spark {
  def getSpark: SparkSession = {
    SparkSession.builder()
      .appName("master")
//      .config("spark.sql.adaptive.enabled", "false")
      .config("spark.sql.codegen.wholeStage", "false")
      .config("spark.eventLog.enabled", "true")
      .config("spark.eventLog.dir", "/tmp/spark-events")
      .config("hive.default.fileformat", "parquet")
      .config("spark.io.compression.codec", "zstd")
      .config("spark.sql.parquet.compression.codec", "zstd")
      .master("local[*]")
      .enableHiveSupport()
      .getOrCreate()
  }

  def init(): Unit = {
    val spark = getSpark
    spark.sql("show tables;").select("tableName").collect().map(_.getString(0)).foreach { t =>
      val schema = spark.sql(s"select * from $t").schema
      spark.read.schema(schema).csv(s"data/$t.dat").createOrReplaceTempView(s"temp$t")
      spark.sql(s"insert overwrite $t select * from temp$t")
    }
  }
}
