import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.count

object datasets extends App {
  val spark = SparkSession.builder()
    .appName("DataFrame Aggregation Example")
    .master("local[*]")
    .getOrCreate()

  val data = Seq(("Alice", 29), ("Bob", 23), ("Charlie", 31), ("Bob", 23))
  val df = spark.createDataFrame(data).toDF("name", "age")

  // Aggregation to count the number of people by age
  df.groupBy("age").agg(count("name").alias("count")).show()

  spark.stop()
}
