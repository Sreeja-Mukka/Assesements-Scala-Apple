import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{col, days, lower}
import org.apache.spark.sql.streaming.Trigger

import scala.collection.mutable.ArrayBuffer

object kafkaSpark extends App {
  val spark = SparkSession.builder
    .appName("kafka-streaming")
    .master("local[2]")
    .getOrCreate()

  val error_data_types = ArrayBuffer("error","serious","critical","stopped","suspended","overflow","issue")


  val kafkaTopic = "logmessages"
  val kafkaBroker = "localhost:9092"

  val df = spark
    .readStream
    .format("kafka")
    .option("kafka.bootstrap.servers" , kafkaBroker)
    .option("subscribe",kafkaTopic)
    .option("startingOffsets","earliest")
    .load()

//  val outputPath1 = "/Users/smukka/plain_data"

  val data  = {
  df.selectExpr("CAST(value AS STRING)")
  }
  val anotherData = {
    df.selectExpr("CAST(value AS STRING)")
  }

//  val query = data
//    .writeStream
//      .outputMode("append")
//      .format("text")
//      .option("path", outputPath1)
//    .option("checkpointLocation", "/Users/smukka/checkpoint1")
//      .trigger(Trigger.ProcessingTime("1 minute"))
//      .start()
//  query.awaitTermination()

  val outputPath1 = "/Users/smukka/error_data1"
  val outputPath2 = "/Users/smukka/good_data1"

  val good_data = data.filter(
    error_data_types.map(word => !lower(col("value")).contains(word)).reduce(_ && _))

  val error_data = anotherData.filter(
    error_data_types.map(word => lower(col("value")).contains(word)).reduce(_ || _))

  val error_query = error_data
    .writeStream
    .outputMode("append")
    .format("text")
    .option("path", outputPath1)
    .option("checkpointLocation", "/Users/smukka/checkpoint1")
    .trigger(Trigger.ProcessingTime("1 minute"))
    .start()

  val query = good_data
  .writeStream
    .outputMode("append")
    .format("text")
    .option("path", outputPath2)
    .option("checkpointLocation", "/Users/smukka/checkpoint2")
    .trigger(Trigger.ProcessingTime("1 minute"))
    .start()

  query.awaitTermination()
  error_query.awaitTermination()


}
