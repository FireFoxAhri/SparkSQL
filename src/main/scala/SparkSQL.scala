import org.apache.spark.sql.SparkSession

object SparkSQL {
  def main(args: Array[String]): Unit = {
    //    val spark = SparkSession.builder()
    //      .appName("master")
    //      .master("local[*]")
    //      .getOrCreate()
    //
    //    import spark.implicits._
    //
    //    val df = spark.read.json("src/main/resources/data/flight-data/json/2010-summary.json")
    //    df.show()

    println(permutation(List(1, 2, 3)))
  }

  def permutation(list: List[Int]): List[List[Int]] = {
    list match {
      case Nil => List(List())
      case ::(head, tail) => for (p0 <- permutation(tail); i <- 0 to p0.length; (xs, ys) = p0 splitAt i)
        yield xs ::: List(head) ::: ys
    }
  }

}
