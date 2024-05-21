file://<WORKSPACE>/Day-6/ListCsvEmpDBThread.scala
### dotty.tools.dotc.core.TypeError$$anon$1: Toplevel definition departmentList is defined in
  <WORKSPACE>/Day-5/ListCsvEmpDB.scala
and also in
  <WORKSPACE>/Day-6/SalesBackTracking.scala
One of these files should be removed from the classpath.

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 3.3.3
Classpath:
<HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala3-library_3/3.3.3/scala3-library_3-3.3.3.jar [exists ], <HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.12/scala-library-2.13.12.jar [exists ]
Options:



action parameters:
offset: 2017
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

import scala.util.{Success,Failure}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

case class Employee(sno:Int , ename:String , ecity:String , esal: Int , edepartment:String)

val filePath = "EmpData.csv"

def readFile(fileName:String ) : List[String] = {
    val EmpDataSource = Source.fromFile(filePath)
    val lines = EmpDataSource.getLines().toList

    EmpDataSource.close()
    lines
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


def addEmpToDepartment(empData:ListBuffer[Employee], con:Connection ) : Unit = {
    val currentTimestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())

    
    val depInfo = HashMap[String,Int]()
    
    try{
        val statement = con.prepareStatement("INSERT INTO empthread (emp_id,emp_name,city,salary,thread_name,timestamp,dep_id) VALUES (?,?,?,?,?,?,?)")
        for(emp <- empData) {
            //println(emp)
              
                var futureResult: Future[Unit] = Future {
                  
                  statement.setInt(1,emp.sno)
                  statement.setString(2,emp.ename)
                  statement.setString(3,emp.ecity)
                  statement.setInt(4,emp.esal)
                  statement.setString(5,Thread.currentThread().getName)
                  statement.setTimestamp(6,currentTimestamp)

                  if(!depInfo.contains(emp.edepartment)){
                    val depIdQuery = con.prepareStatement("selec@@t id from department where dep_name = ?")
                    depIdQuery.setString(1,emp.edepartment)
                    val res = depIdQuery.executeQuery()
                
                    while(res.next()) {
                      val id = res.getInt("id")
                      depInfo.put(emp.edepartment,id)
                      statement.setInt(7,id)
                    }
                  }
                  else{
                    statement.setInt(7,depInfo.get(emp.edepartment).getOrElse(-1))
                  }
                    statement.executeUpdate()
              }        
          }
        
        statement.close()
    }
    catch {
      case e: Exception => println(e.printStackTrace())
    } finally {
      // Close Statement and Connection
      con.close()
    }
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

dotty.tools.dotc.core.TypeError$$anon$1: Toplevel definition departmentList is defined in
  <WORKSPACE>/Day-5/ListCsvEmpDB.scala
and also in
  <WORKSPACE>/Day-6/SalesBackTracking.scala
One of these files should be removed from the classpath.