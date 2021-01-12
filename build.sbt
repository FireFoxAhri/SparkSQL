name := "Spark"

version := "0.1"

scalaVersion := "2.12.12"

val sparkVersion = "3.0.1"
val hadoopVersion = "3.3.0"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.spark" %% "spark-graphx" % sparkVersion,
  "org.apache.spark" %% "spark-mllib" % sparkVersion,
  "org.apache.spark" %% "spark-streaming-kafka-0-10" % sparkVersion,
  "org.apache.hadoop" % "hadoop-client" % hadoopVersion,
  "org.apache.hadoop" % "hadoop-hdfs" % hadoopVersion,

  "org.apache.kafka" % "kafka-clients" % "2.7.0",
  "org.scalanlp" %% "breeze" % "1.1",
  "com.github.scopt" %% "scopt" % "4.0.0"
)