import org.apache.spark.sql.SparkSession

object dataset_red extends App {
  val spark = SparkSession.builder()
    .appName("Dataset Reduction Example")
    .master("local[*]")
    .getOrCreate()

  import spark.implicits._

  val data = Seq(1, 2, 3, 4, 5)
  val dataset = spark.createDataset(data)

  // Use reduce to sum all elements
  val totalSum = dataset.reduce(_ + _)
  println(s"Total Sum: $totalSum")

  spark.stop()
}
