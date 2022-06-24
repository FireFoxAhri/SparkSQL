import Spark.getSpark

object SparkSQL {
  def main(args: Array[String]): Unit = {
    val spark = getSpark

    spark.sql("show tables;").show(100, truncate = false)
    while (true) Thread.sleep(10000)
  }

}
