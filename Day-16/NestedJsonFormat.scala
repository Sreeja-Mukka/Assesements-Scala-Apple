import org.apache.spark.sql.{SparkSession, DataFrame}
import org.apache.spark.sql.functions._

object NestedJsonFormat {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("MySQL Integration Example")
      .master("local[*]")
      .getOrCreate()

    val jdbcUrl = "jdbc:mysql://34.168.64.183:3306/json_s3"
    val connectionProperties = new java.util.Properties()
    connectionProperties.setProperty("user", "******")
    connectionProperties.setProperty("password", "********")

    val df1 = spark.read.jdbc(jdbcUrl, "genre", connectionProperties)
    val df2 = spark.read.jdbc(jdbcUrl, "book", connectionProperties)

    val joinedDF = df1.join(df2, df1("gid") === df2("bgen_id"))
    joinedDF.show()

    import spark.implicits._

    joinedDF.toDF("gid","gname","bid","bname","bauthor","bgen_id")

    val groupedDF = joinedDF.groupBy($"gid", $"gname")
      .agg(collect_list(struct($"bid", $"bname", $"bauthor",$"bgen_id")).as("books"))

    val jsonDF = groupedDF.toJSON
    jsonDF.show(false)
    //jsonDF.write.json("hdfs:///Books_groupBy_Genre_json")

    spark.stop()
  }
}