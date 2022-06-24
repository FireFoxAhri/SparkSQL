import org.apache.spark.{HashPartitioner, RangePartitioner}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.storage.StorageLevel

import scala.util.Random

object RDDTest {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("master")
      .config("spark.sql.adaptive.enabled", "false")
      .master("local[*]")
      .getOrCreate()

    val sc = spark.sparkContext

    //    val rdd = sc.textFile("src/main/resources/data/flight-data/csv/2010-summary.csv", 4)
    //    rdd.persist(StorageLevel.MEMORY_ONLY)
    //    val array = rdd.map(_.split(' ')).groupBy(_ (0)).map {
    //      case (key, iter) => (key, iter.size)
    //    }.sortByKey().collect()
    //    array.foreach(a => println(a))


    val rdd: RDD[(Int, Int)] = sc.parallelize((1 to 20).map(_ => (Random.nextInt(100), Random.nextInt(10))))
    rdd.sortByKey().collect().foreach(println)
    //    rdd.glom().collect().foreach(a => println(a.mkString("Array(", ", ", ")")))
    //rdd.glom().collect().foreach(a => println(a.mkString("Array(", ", ", ")")))
  }
}
