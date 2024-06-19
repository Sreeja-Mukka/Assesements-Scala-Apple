import org.apache.spark.sql.SparkSession

object rdd_agg extends App {
  val spark = SparkSession.builder()
    .appName("RDD Basic Aggregation")
    .master("local[*]")
    .getOrCreate()

  val data = Seq(1, 2, 3, 4, 5)
  val rdd = spark.sparkContext.parallelize(data)

  // Define the zero value and the operation for aggregation
  val sumCount = rdd.aggregate((0, 0))(
    (acc, value) => (acc._1 + value, acc._2 + 1),
    (part1, part2) => (part1._1 + part2._1, part1._2 + part2._2)
  )

  val average = sumCount._1.toDouble / sumCount._2
  println(s"Average: $average")

  spark.stop()
}
