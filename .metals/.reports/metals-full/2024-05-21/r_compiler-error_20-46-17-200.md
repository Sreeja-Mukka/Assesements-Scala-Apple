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
offset: 1273
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


def addEmpDataThread(emp:Employee , con:Connection) = { 
    val timestamp = Timestamp.valueOf(LocalDat@@eTime.now())
    val statement:PreparedStatement = null
    
  
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
    var con:Connection = jdbcConnection
    addThreadEmp(EmpData,con)
    
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