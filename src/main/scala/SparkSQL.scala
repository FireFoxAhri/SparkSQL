import org.apache.spark.sql.SparkSession

object SparkSQL {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("master")
      .config("spark.sql.adaptive.enabled", "false")
      .master("local[*]")
      .getOrCreate()

    val df = spark.read.json("src/main/resources/data/flight-data/json/2010-summary.json")
    df.createOrReplaceTempView("summary")
    spark.sql("select count(*) from summary").show(100, truncate = false)

  }

}
