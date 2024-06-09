import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object s3JsonToDb {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("S3 Json to MySQL")
      .master("local[*]")
      .config("spark.sql.warehouse.dir", "file:///tmp/spark-warehouse")
      .config("spark.hadoop.fs.s3a.aws.credentials.provider", "org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider")
      .getOrCreate()

    val mysqlUrl = "jdbc:mysql://34.168.64.183:3306/json_s3"
    val mysqlUser = "smukka"
    val mysqlPassword = "Shiva9090$"
    val mysqlTable = "people"
    val s3Bucket = "scala-akka-bucket"
    val s3FilePath = "People.jsonl"

    val jsonData = spark.read
      .option("inferSchema", "true")
      .json(s"s3a://$s3Bucket/$s3FilePath")
    // Process data
    val filteredData = jsonData.filter(col("city") === "London")

    filteredData.write
      .format("jdbc")
      .option("url", mysqlUrl)
      .option("dbtable", mysqlTable)
      .option("user", mysqlUser)
      .option("password", mysqlPassword)
      .mode("append")
      .save()
    // Show processed data
    filteredData.show()
    spark.stop()
  }
}
