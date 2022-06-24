name := "SparkSQL"

version := "0.1"

scalaVersion := "2.13.7"

val sparkVersion = "3.3.0"
val hadoopVersion = "3.3.3"


libraryDependencies ++= Seq(
  "org.apache.hadoop" % "hadoop-client" % hadoopVersion,
  "org.apache.hadoop" % "hadoop-client-api" % hadoopVersion,
  "org.apache.hadoop" % "hadoop-client-runtime" % hadoopVersion,
  "org.apache.hadoop" % "hadoop-hdfs" % hadoopVersion,

  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.spark" %% "spark-yarn" % sparkVersion,
  "org.apache.spark" %% "spark-hive" % sparkVersion,
  "org.apache.spark" %% "spark-graphx" % sparkVersion,
  "org.apache.spark" %% "spark-mllib" % sparkVersion,
  "org.apache.spark" %% "spark-streaming-kafka-0-10" % sparkVersion,

  "org.apache.kafka" % "kafka-clients" % "3.1.0",
  "org.scalanlp" %% "breeze" % "2.0",
  "com.github.scopt" %% "scopt" % "4.0.1"
)