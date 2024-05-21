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
offset: 849
uri: file://<WORKSPACE>/Day-6/ListCsvEmpDBThread.scala
text:
```scala
import scala.io.Source
import scala.collection.mutable.ListBuffer
import java.sql.Connection
import java.sql.Statement
import java.sql.DriverManager
import java.sql.Timestamp
import java.time.LocalDateTime
import scala.collection.mutable.HashMap
import java.util.concurrent._

case class Employee(sno:Int , ename:String , ecity:String , esal: Int , edepartment:String)
val filePath = "EmpData.csv"
def readFile(fileName:String ) : List[String] = {
    val EmpDataSource = Source.fromFile(filePath)
    val lines = EmpDataSource.getLines().toList

    EmpDataSource.close()
    lines
}
var departmentList = ListBuffer[String]() 

def filterDep(Empdata: ListBuffer[Employee])= {
    val groupByDep = Empdata.groupBy(_.edepartment)
    departmentList ++= groupByDep.keySet
}

def jdbcConnection:Connection  = {
    Class.forName("com.mysql.cj.jdbc.Driv@@er")

    // Establish a connection
    val url = "jdbc:mysql://hadoop-server.mysql.database.azure.com:3306/sreeja"
    val username = "sqladmin"
    val password = "Password@12345"
    val connection: Connection = DriverManager.getConnection(url, username, password)
    connection
}

class AddEmpThread(emp:Employee,con:Connection) extends Runnable {  
    val timestamp: LocalDateTime = Timestamp.valueOf(LocalDateTime.now())
    override def run(): Unit = {
      val sqlQuery = "INSERT INTO employee (emp_id,emp_name,city,salary,thread_name,timestamp,dep_id) VALUES (?,?,?,?,?,?,?)"
      val statement = con.prepareStatement(sqlQuery)
      statement.setInt(1,emp.sno)
      statement.setString(2,emp.ename)
      statement.setString(3,emp.ecity)
      statement.setInt(4,emp.esal)
      statement.setString(5,Thread.currentThread().getName)
      statement.setTimestamp(timestamp)


    }
}




def addThreadEmp(Empdata:ListBuffer[Employee],con:Connection ) : Unit = {
  val pool: ExecutorService = Executors.newFixedThreadPool(5)
  Empdata.foreach(emp => pool.execute(new AddEmpThread(emp,con)))
}


@main def EmpCsvMain = {
    
    var EmpData = ListBuffer[Employee]()

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
    
    var con:Connection = jdbcConnection
    addEmpToDepartment(EmpData,con)
    
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