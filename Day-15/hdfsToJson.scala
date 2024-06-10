import org.apache.spark.sql.{SparkSession}
import org.apache.spark.sql.functions._

object hdfsToJson {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("CSV to JSON Converter")
      .getOrCreate()

    val inputPath = "hdfs:///employees.csv"
    val outputPath = "hdfs:///emp_json"

    val df = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv(inputPath)

    val filteredDf = df.filter(col("department") === "Finance")

    filteredDf.write
      .mode("overwrite")
      .json(outputPath)

    val mysqlUrl = "jdbc:mysql://34.168.64.183:3306/json_s3"
    val mysqlUser = "******"
    val mysqlPassword = "********"
    val mysqlTable = "employee"

    filteredDf.write.format("jdbc")
      .option("url", mysqlUrl)
      .option("dbtable", mysqlTable)
      .option("user", mysqlUser)
      .option("password", mysqlPassword)
      .option("driver", "com.mysql.cj.jdbc.Driver")
      .mode("overwrite")
      .save()

    spark.stop()
  }
}