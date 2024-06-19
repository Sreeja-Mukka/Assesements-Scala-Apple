import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.max

object DataFrameMaxExample extends App {
  val spark = SparkSession.builder()
    .appName("DataFrame Max Example")
   .master("local[*]")
    .getOrCreate()

  val data = Seq(("Alice", 29), ("Bob", 23), ("Charlie", 31), ("David", 25))
  val df = spark.createDataFrame(data).toDF("name", "age")

  // Find the maximum age
  df.select(max("age")).show()

  spark.stop()
}
