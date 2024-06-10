import org.apache.spark.sql.{SparkSession, SaveMode}
import org.apache.spark.sql.functions._

object dbTos3 {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("Spark db to S3")
      .master("local[*]")
      .config("spark.sql.warehouse.dir", "file:///tmp/spark-warehouse")
      .config("spark.hadoop.fs.s3a.aws.credentials.provider", "org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider")
      .getOrCreate()

    // Replace these with your MySQL and S3 credentials
    val mysqlUrl = "jdbc:mysql://34.168.64.183:3306/json_s3"
    val mysqlUser = "*******"
    val mysqlPassword = "*******"
    val mysqlTable = "animal"
    val s3Bucket = "scala-akka-bucket"
    val s3OutputPath = s"s3a://$s3Bucket/output1.csv"

    // Read data from MySQL
    val jdbcDF = spark.read
      .format("jdbc")
      .option("url", mysqlUrl)
      .option("dbtable", mysqlTable)
      .option("user", mysqlUser)
      .option("password", mysqlPassword)
      .load()

    // Perform filter or aggregate operations (example: filter where age > 30)
    val filteredDF = jdbcDF.filter(col("atype") === "Dog")

    // Write the DataFrame to CSV format
    filteredDF.write
      .option("header", "true")
      .csv("/tmp/output1.csv")

    // Upload the CSV to S3
    val csvDF = spark.read
      .option("header", "true")
      .csv("/tmp/output1.csv")

    csvDF.write
      .mode(SaveMode.Overwrite)
      .csv(s3OutputPath)

    spark.stop()
  }
}
