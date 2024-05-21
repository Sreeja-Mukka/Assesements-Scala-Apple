file://<WORKSPACE>/Day-6/ListCsvEmpDBThread.scala
### dotty.tools.dotc.core.TypeError$$anon$1: Toplevel definition addEmpToDepartment is defined in
  <WORKSPACE>/Day-6/SalesBackTracking.scala
and also in
  <WORKSPACE>/Day-5/ListCsvEmpDB.scala
One of these files should be removed from the classpath.

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 3.3.3
Classpath:
<HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala3-library_3/3.3.3/scala3-library_3-3.3.3.jar [exists ], <HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.12/scala-library-2.13.12.jar [exists ]
Options:



action parameters:
offset: 842
uri: file://<WORKSPACE>/Day-6/ListCsvEmpDBThread.scala
text:
```scala
import scala.io.Source
import java.util.concurrent._
import scala.collection.mutable.ListBuffer
import java.sql.Connection
import java.sql.Statement
import java.sql.DriverManager
import java.sql.Timestamp
import java.time.LocalDateTime
import scala.collection.mutable.HashMap
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Future,Await}
import java.sql.PreparedStatement


case class Employee(sno:Int , ename:String , ecity:String , esal: Int , edepartment:String)
val filePath = "EmpData.csv"
def readFile(fileName:String ) : List[String] = {
    val EmpDataSource = Source.fromFile(filePath)
    val lines = EmpDataSource.getLines().toList

    EmpDataSource.close()
    lines
}
var departmentList = ListBuffer[String]() 
val depInfo = HashMap[String,Int]()
def DepMap(@@depList:ListBuffer[String]) = {
  val con:Connection  = jdbcConnection


}
def filterDep(Empdata: ListBuffer[Employee])= {
    val groupByDep = Empdata.groupBy(_.edepartment)
    departmentList ++= groupByDep.keySet
}

def jdbcConnection:Connection  = {
    Class.forName("com.mysql.cj.jdbc.Driver")

    // Establish a connection
    val url = "jdbc:mysql://hadoop-server.mysql.database.azure.com:3306/sreeja"
    val username = "sqladmin"
    val password = "Password@12345"
    val connection: Connection = DriverManager.getConnection(url, username, password)
    connection
}


def addEmpDataThread(empData:ListBuffer[Employee]) = { 
    val pool: ExecutorService = Executors.newFixedThreadPool(4)

    val futures = empData.map(employee => Future {
      insertEmployee(employee,Thread.currentThread().getName)
    })
    Await.result(Future.sequence(futures), Duration.Inf)
  
}
def insertEmployee(emp:Employee,threadName:String):Unit = {
 var con:Connection = null
 var statement:PreparedStatement = null 
 try {
  con = jdbcConnection
  val query = "INSERT INTO empthread (emp_id,emp_name,city,salary,thread_name,timestamp,dep_id) VALUES (?,?,?,?,?,?,?)"
  statement=con.prepareStatement(query)
  val scalaTimestamp = Timestamp.valueOf(LocalDateTime.now())

  statement.setInt(1,emp.sno)
  statement.setString(2,emp.ename)
  statement.setString(3,emp.ecity)
  statement.setInt(4,emp.esal)
  statement.setString(5,threadName)
  statement.setTimestamp(6,scalaTimestamp)
  statement.setInt(7,departmentList.indexOf(emp.edepartment))
  statement.executeUpdate()
 }
 catch {
  case e: Exception => e.printStackTrace()
 }
 finally {
  if(statement!=null) statement.close()
  if(con!=null) con.close()
 }
}

@main def EmpCsvMain = {
    
    var EmpData :ListBuffer[Employee] = ListBuffer()

    val data:List[String] = readFile(filePath)

    val EmpDataObj = data.tail.map(_.split(",")).map {
        fields => 
            val sno = fields(0).toInt
            val name = fields(1)
            val city = fields(2)
            val salary = fields(3).toInt
            val department = fields(4)
            val obj:Employee = Employee(sno,name,city,salary,department)
            
            EmpData+=obj
    }
    
    filterDep(EmpData)
    println(departmentList.indexOf(""))
    //addEmpDataThread(EmpData)
    
} 
```



#### Error stacktrace:

```

```
#### Short summary: 

dotty.tools.dotc.core.TypeError$$anon$1: Toplevel definition addEmpToDepartment is defined in
  <WORKSPACE>/Day-6/SalesBackTracking.scala
and also in
  <WORKSPACE>/Day-5/ListCsvEmpDB.scala
One of these files should be removed from the classpath.