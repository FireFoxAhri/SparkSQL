import scala.reflect.ClassTag

object ClassTagTest {
  def main(args: Array[String]): Unit = {
    def mkArray[T: ClassTag](elems: T*): Unit = Array[T](elems: _*)

    val test = mkArray(123, 456)
    println("")
  }

}
