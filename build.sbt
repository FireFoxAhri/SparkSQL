name := "SparkSQL"

version := "0.1"

scalaVersion := "2.13.6"

val sparkVersion = "3.2.0-SNAPSHOT"
val hadoopVersion = "3.3.0"

resolvers += "Apache Snapshots" at "https://repository.apache.org/content/repositories/snapshots"

libraryDependencies ++= Seq(
  "org.apache.hadoop" % "hadoop-client" % hadoopVersion,
  "org.apache.hadoop" % "hadoop-client-api" % hadoopVersion,
  "org.apache.hadoop" % "hadoop-client-runtime" % hadoopVersion,
  "org.apache.hadoop" % "hadoop-hdfs" % hadoopVersion,

  "org.apache.spark" %% "spark-sql" % sparkVersion changing(),
  "org.apache.spark" %% "spark-hive" % sparkVersion changing(),
  "org.apache.spark" %% "spark-graphx" % sparkVersion changing(),
  "org.apache.spark" %% "spark-mllib" % sparkVersion changing(),
  "org.apache.spark" %% "spark-streaming-kafka-0-10" % sparkVersion changing(),

  "org.apache.kafka" % "kafka-clients" % "2.8.0",
  "org.scalanlp" %% "breeze" % "1.1",
  "com.github.scopt" %% "scopt" % "4.0.1"
)