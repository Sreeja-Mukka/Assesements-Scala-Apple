import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions.{col, split}

object  ThreeTables extends App {
    val spark = SparkSession.builder
      .appName("Three tables to one Table")
      .master("local[*]")
      .config("spark.cassandra.connection.host","cassandra.us-east-1.amazonaws.com")
      .config("spark.cassandra.connection.port", "9142")
      .config("spark.cassandra.connection.ssl.enabled", "true")
      .config("spark.cassandra.auth.username", "****************")
      .config("spark.cassandra.auth.password", "*******************")
      .config("spark.cassandra.input.consistency.level", "LOCAL_QUORUM")
      .config("spark.cassandra.connection.ssl.trustStore.path", "/Users/smukka/cassandra_truststore.jks")
      .config("spark.cassandra.connection.ssl.trustStore.password", "*****************")
      .getOrCreate()

  val products = "/Users/smukka/Downloads/Products.csv"
  val customers = "/Users/smukka/Downloads/Customers.csv"
  val sales = "/Users/smukka/Downloads/Sales.csv"

  val proddf = spark.read
    .option("header", "true")
    .option("inferSchema", "true")
    .csv(products)
    .withColumnRenamed("name","product_name")

  val custdf = spark.read
    .option("header", "true")
    .option("inferSchema", "true")
    .csv(customers)
    .withColumnRenamed("name","customer_name")

  val salesdf = spark.read
    .option("header", "true")
    .option("inferSchema", "true")
    .csv(sales)

  val salesByProd = salesdf.join(proddf , salesdf("product_id")===proddf("product_id"))

  val finalSalesByProd = salesByProd.drop(proddf("product_id"))
  //finalSalesByProd.show()

  val salesByCus = salesdf.join(custdf , salesdf("customer_id")===custdf("customer_id"))

  val finalSalesByCus = salesByCus.drop(custdf("customer_id"))
  //finalSalesByCus.show()

  val final_table = finalSalesByCus.join(finalSalesByProd, finalSalesByProd("transaction_id") === finalSalesByCus("transaction_id"))

  val res = final_table.drop(finalSalesByProd("transaction_id")).drop(finalSalesByProd("product_id"))
  .drop(finalSalesByProd("customer_id"))
  .drop(finalSalesByCus("customer_id"))
  .drop(finalSalesByCus("units"))
  .drop(finalSalesByCus("address"))
  .drop(finalSalesByCus("city"))
  .drop(finalSalesByCus("customer_id"))
  //res.show()

  def calculateTheSalesAmount(df : DataFrame ): DataFrame = {
    df.withColumn("salesAmount",col("units")*col("price")).drop(col("units")).drop(col("price"))
  }

  val ordered_df = res.select(col("transaction_id"),col("product_id"),col("product_name"),col("customer_name"),col("units"),col("price"))
  //ordered_df.show()

  val res_df = calculateTheSalesAmount(ordered_df)
  res_df.show()

//  val cassandra_df =
//    res_df.write
//      .format("org.apache.spark.sql.cassandra")
//      .options(Map("table" -> "SalesCFamily", "keyspace" -> "tutorialkeyspace"))
//      .mode("append")
//      .save()
  spark.close()
}
