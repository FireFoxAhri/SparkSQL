import org.apache.spark.sql.SparkSession

object AQE {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("master")
      .config("spark.sql.adaptive.enabled", "false")
      .config("spark.sql.adaptive.forceApply", "true")
      .config("spark.sql.adaptive.coalescePartitions.enabled", "true")
      .config("spark.sql.adaptive.coalescePartitions.minPartitionNum", "1")
      .config("spark.sql.adaptive.coalescePartitions.initialPartitionNum", "200")
      .config("spark.sql.adaptive.advisoryPartitionSizeInBytes", "256MB")
      .master("local[*]")
      .getOrCreate()
    import spark.implicits._
    //    val df = spark.read.option("header", "true").csv("aqe.csv")
    //    df.createOrReplaceTempView("data")
    //    spark.sql("select * from data limit 100").show()

    //    spark.sql(
    //      """CREATE TABLE items
    //        |USING parquet
    //        |  AS
    //        |SELECT id AS i_item_id,
    //        |CAST(rand() * 1000 AS INT) AS i_price
    //        |FROM RANGE(30000000);""".stripMargin)
    //
    //    spark.sql(
    //      """CREATE TABLE sales
    //        |USING parquet
    //        |AS
    //        |SELECT CASE WHEN rand() < 0.8 THEN 100 ELSE CAST(rand() * 30000000 AS INT) END AS s_item_id,
    //        |CAST(rand() * 100 AS INT) AS s_quantity,
    //        |DATE_ADD(current_date(), - CAST(rand() * 360 AS INT)) AS s_date
    //        |FROM RANGE(1000000000);""".stripMargin)

    spark.read.parquet("spark-warehouse/items").createOrReplaceTempView("items")
    spark.read.parquet("spark-warehouse/sales").createOrReplaceTempView("sales")

    //    spark.sql(
    //      """SELECT s_date, sum(s_quantity) AS q
    //        |FROM sales
    //        |GROUP BY s_date
    //        |ORDER BY q DESC;""".stripMargin).write.parquet("spark-warehouse/result1-2")


    spark.sql(
      """SELECT s_date, sum(s_quantity * i_price) AS total_sales
        |FROM sales
        |JOIN items ON i_item_id = s_item_id
        |GROUP BY s_date
        |ORDER BY total_sales DESC;""".stripMargin).show()
    println("")
  }

}
