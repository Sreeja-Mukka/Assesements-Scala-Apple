import org.apache.spark.sql.SparkSession
import org.scalatest.funsuite.AnyFunSuite
import ThreeTables.calculateTheSalesAmount

class ThreeTablesTest extends AnyFunSuite {

  val spark = SparkSession.builder()
    .master("local[*]")
    .appName("Spark Tests")
    .getOrCreate()

  test("test-case1")  {

    import spark.implicits._
    val SalesData = Seq(
      (5, 1, "prod-a", "cus-a", 4, 12),
      (2, 4, "prod-b", "cus-b", 3, 6),
      (3, 3, "prod-c", "cus-c", 2, 15),
      (1, 2, "prod-d", "cus-d", 1, 10),
    ).toDF("transaction_id", "product_id", "product_name", "customer_name", "units", "price")

    val res = calculateTheSalesAmount(SalesData)

    val expected_data = Seq(
      (5, 1, "prod-a", "cus-a", 48),
      (2, 4, "prod-b", "cus-b", 18),
      (3, 3, "prod-c", "cus-c", 30),
      (1, 2, "prod-d", "cus-d", 10),
    ).toDF("transaction_id", "product_id", "product_name", "customer_name", "SalesAmount")

    assert(res.collect() sameElements expected_data.collect(), "the test failed as the data is not valid")
  }
}
