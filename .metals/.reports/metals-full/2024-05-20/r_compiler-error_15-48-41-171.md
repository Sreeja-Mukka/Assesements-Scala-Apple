file://<WORKSPACE>/Day-5/ListCsvEmpDB.scala
### java.lang.StringIndexOutOfBoundsException: Range [2264, 2257) out of bounds for length 3373

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 3.3.3
Classpath:
<HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala3-library_3/3.3.3/scala3-library_3-3.3.3.jar [exists ], <HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.12/scala-library-2.13.12.jar [exists ]
Options:



action parameters:
offset: 2257
uri: file://<WORKSPACE>/Day-5/ListCsvEmpDB.scala
text:
```scala
import scala.io.Source
import scala.collection.mutable.ListBuffer
import java.sql.Connection
import java.sql.Statement
import java.sql.DriverManager
import scala.collection.mutable.HashMap

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

def addDepDataToTable(depList:ListBuffer[String], con:Connection) = {
    val statement = con.prepareStatement("INSERT INTO department (dep_name) VALUES (?)")
    try {
      // Create a statement
      //val statement: Statement = con.createStatement()
      for(dep <- depList) {
        //println(dep)
        statement.setString(1,dep)
        statement.executeUpdate()
      }

    }
    catch {
      case e: Exception => e.printStackTrace()
    } finally {
      // Close Statement and Connection
      statement.close()
      con.close()
    }
    println("Data added!")
}

def addEmpToDepartment(empData:ListBuffer[Employee], con:Connection ) : Unit = {
    val statement = con.prepareStatement("INSERT INTO employee (emp_name,city,salary,dep_id) VALUES (?,?,?,?)")
    val depInfo = HashMap[String,Int]()
    try{
        for(emp <- empData) {
            statement.setString(1,emp.ename)
            statement.setString(2,emp.ecity)
            statement.setInt(3,emp.esal)
            if(!depInfo.contains(emp.edepartment)){
                val depId = con.prepareStatement("select id from department where dep_name = ?")
                depId.setString(1,emp.edepartment)
                if (r@@.next()) {
                val res = depId.executeQuery()
                depInfo.put(emp.edepartment ,res.getInt("id"))
            }
            statement.setInt(4,depInfo.get(emp.edepartment).getOrElse(0))
            statement.executeUpdate()
        }
    }
    catch {
      case e: Exception => e.printStackTrace()
    } finally {
      // Close Statement and Connection
      statement.close()
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
    
    filterDep(EmpData)
    //var con:Connection = jdbcConnection
    //addDepDataToTable(departmentList,con)
    var con:Connection = jdbcConnection
    addEmpToDepartment(EmpData,con)
} 
```



#### Error stacktrace:

```
java.base/jdk.internal.util.Preconditions$1.apply(Preconditions.java:55)
	java.base/jdk.internal.util.Preconditions$1.apply(Preconditions.java:52)
	java.base/jdk.internal.util.Preconditions$4.apply(Preconditions.java:213)
	java.base/jdk.internal.util.Preconditions$4.apply(Preconditions.java:210)
	java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:98)
	java.base/jdk.internal.util.Preconditions.outOfBoundsCheckFromToIndex(Preconditions.java:112)
	java.base/jdk.internal.util.Preconditions.checkFromToIndex(Preconditions.java:349)
	java.base/java.lang.String.checkBoundsBeginEnd(String.java:4865)
	java.base/java.lang.String.substring(String.java:2834)
	scala.meta.internal.pc.completions.CompletionPos$.infer(CompletionPos.scala:57)
	scala.meta.internal.pc.completions.CompletionPos$.infer(CompletionPos.scala:47)
	scala.meta.internal.pc.completions.CompletionProvider.completions(CompletionProvider.scala:64)
	scala.meta.internal.pc.ScalaPresentationCompiler.complete$$anonfun$1(ScalaPresentationCompiler.scala:147)
```
#### Short summary: 

java.lang.StringIndexOutOfBoundsException: Range [2264, 2257) out of bounds for length 3373