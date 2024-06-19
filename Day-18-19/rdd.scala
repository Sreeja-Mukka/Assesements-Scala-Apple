import org.apache.spark.sql.SparkSession

object rdd extends App {
  val spark = SparkSession.builder()
    .appName("RDD Aggregation Example")
    .master("local[*]")
    .getOrCreate()

  val data = Seq(("Alice", 29), ("Bob", 23), ("Charlie", 31))
  val rdd = spark.sparkContext.parallelize(data)

  // Aggregation using reduceByKey to calculate average age
  val mappedRdd = rdd.mapValues(age => (age, 1))
  val reducedRdd = mappedRdd.reduceByKey((x, y) => (x._1 + y._1, x._2 + y._2))
  val averageAgeRdd = reducedRdd.mapValues { case (sum, count) => sum / count }

  averageAgeRdd.collect().foreach(println)

  spark.stop()
}
