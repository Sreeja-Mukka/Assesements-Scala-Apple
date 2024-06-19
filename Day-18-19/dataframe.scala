import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.avg

case class Person(name: String, age: Int)

object dataframe extends App {
  val spark = SparkSession.builder()
    .appName("Dataset Aggregation Example")
    .master("local[*]")
    .getOrCreate()

  import spark.implicits._

  val data = Seq(Person("Alice", 29), Person("Bob", 23), Person("Charlie", 31))
  val dataset = spark.createDataset(data)

  // Aggregation to calculate average age
  dataset.groupBy().agg(avg("age").alias("average_age")).show()

  spark.stop()
}
