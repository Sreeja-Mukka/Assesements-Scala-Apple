import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.broadcast
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions._

object BroadcastingSpark extends App {
    val spark = SparkSession.builder
        .appName("BroadCast-Spark")
        .master("local[*]")
        .getOrCreate()

    val transactionDataPath = "hdfs:///transaction_data.csv"
    val usersDataPath = "hdfs:///user_data.csv"
    val GroupedDataPath = "hdfs:///grouped_data"
    val GroupedByBroadcastPath = "hdfs:///broadcast_groupedData"

    val transactionsData = spark.read
    .option("header", "true")
    .option("inferSchema", "true")
    .csv("/var/folders/75/7280jgqx50vclm6n4_hc9lz40000gp/T/spark-optional/sparl/src/main/resources/transaction_data.csv")
    
    val usersData = spark.read
    .option("header","true")
    .option("inferSchema", "true")
    .csv("/var/folders/75/7280jgqx50vclm6n4_hc9lz40000gp/T/spark-optional/sparl/src/main/resources/user_data.csv")

//     val startTimeWithoutBroadcast = System.nanoTime()
    val joinedDF = transactionsData.join(usersData, "user_id")
    // val groupByResult = joinedDF.groupBy("name").sum("amount")
//     val endTimeWithoutBroadcast = System.nanoTime()
//   println(s"Time without broadcast: ${(endTimeWithoutBroadcast - startTimeWithoutBroadcast) / 1e9d} seconds")

//     groupByResult.write
//     .option("header","true")
//     .csv(GroupedDataPath)


//     val startTimeWithBroadcast = System.nanoTime()
    val broadcastedUsers = broadcast(usersData)
    val joinWithBroadcast = transactionsData.join(broadcastedUsers, "user_id")
//     val groupByBroadcastResult = joinWithBroadcast.groupBy("name").sum("amount")
//     val endTimeWithBroadcast = System.nanoTime()
//   println(s"Time without broadcast: ${(endTimeWithBroadcast - startTimeWithBroadcast) / 1e9d} seconds")

//     groupByBroadcastResult.write
//     .option("header","true")
//     .csv(GroupedByBroadcastPath)
    
    val startTimeWithoutWindow = System.nanoTime()
    val windowSpec = Window.partitionBy("name")
    val groupByWithoutWindow = joinedDF.withColumn("total_amount", sum("amount").over(windowSpec)).select("name", "total_amount").distinct()
    
    val endTimeWithoutWindow = System.nanoTime()
    println(s"Time with window partitioning: ${(endTimeWithoutWindow - startTimeWithoutWindow) / 1e9d} seconds")

    groupByWithoutWindow.write.option("header", "true").csv("/var/folders/75/7280jgqx50vclm6n4_hc9lz40000gp/T/spark-optional/sparl/src/main/resources/windowPartition")

    // val startTimeWithWindow = System.nanoTime()
    // val windowSpec = Window.partitionBy("name")
    // val groupByWithWindow = joinWithBroadcast.withColumn("total_amount", sum("amount").over(windowSpec)).select("name", "total_amount").distinct()
    
    // val endTimeWithWindow = System.nanoTime()
    // println(s"Time with window partitioning: ${(endTimeWithWindow - startTimeWithWindow) / 1e9d} seconds")
    // groupByWithWindow.write.option("header", "true").csv("/var/folders/75/7280jgqx50vclm6n4_hc9lz40000gp/T/spark-optional/sparl/src/main/resources/windowPartitionByPartition")

    Thread.sleep(100000)
    spark.stop()
}