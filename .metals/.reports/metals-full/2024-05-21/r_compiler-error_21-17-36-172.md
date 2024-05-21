file://<WORKSPACE>/Day-6/SalesBackTracking.scala
### dotty.tools.dotc.core.TypeError$$anon$1: Toplevel definition filterDep is defined in
  <WORKSPACE>/Day-5/ListCsvEmpDB.scala
and also in
  <WORKSPACE>/Day-6/ListCsvEmpDBThread.scala
One of these files should be removed from the classpath.

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 3.3.3
Classpath:
<HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala3-library_3/3.3.3/scala3-library_3-3.3.3.jar [exists ], <HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.12/scala-library-2.13.12.jar [exists ]
Options:



action parameters:
offset: 931
uri: file://<WORKSPACE>/Day-6/SalesBackTracking.scala
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

def jdbcConnection:Connection  = {
    Class.forName("com.mysql.cj.jdbc.Driver")

    // Establish a connection
    val url = "jdbc:mysql://hadoop-server.mysql.database.azure.com:3306/sreeja"
    val username = "sqladmin"
    val password = "Password@12345"
    val connection: Connection = DriverManager.getConnection(url, username, password)
    connection
}

def addEmpToDepartment(em@@pData:ListBuffer[Employee], con:Connection ) : Unit = {
    val statement = con.prepareStatement("INSERT INTO employee (emp_id,emp_name,city,salary,dep_id) VALUES (?,?,?,?,?)")
    val depInfo = HashMap[String,Int]()
    try{
        for(emp <- empData) {
            statement.setInt(1,emp.sno)
            statement.setString(2,emp.ename)
            statement.setString(3,emp.ecity)
            statement.setInt(4,emp.esal)
            if(!depInfo.contains(emp.edepartment)){
                val depIdQuery = con.prepareStatement("select id from department where dep_name = ?")
                depIdQuery.setString(1,emp.edepartment)
                val res = depIdQuery.executeQuery()
                
                while(res.next()) {
                    val id = res.getInt("id")
                    depInfo.put(emp.edepartment,id)
                    statement.setInt(5,id)
                }
            }
            else{
                statement.setInt(5,depInfo.get(emp.edepartment).getOrElse(-1))
            }
            //statement.setInt(4,depInfo.get(emp.edepartment).getOrElse(0))
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
    println("Finished")
}


var empSales = ListBuffer[String]() 
def getRowsOfDepartment(dep_id:Int,con:Connection) : Unit = {
    val statement = con.prepareStatement("select * from employee where dep_id = ?")
    try{
    statement.setInt(1,dep_id) 
    println()
    //println(s"The List of Employees in the Department $dep_id are :")
    println()
    val res = statement.executeQuery()
    while(res.next()) 
    {
        val emp_name = res.getString("emp_name")
        empSales.addOne(emp_name)
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

def backTrack(empSale:ListBuffer[String],res:ListBuffer[ListBuffer[String]],partition:ListBuffer[String], start:Int, end:Int) : Unit = {
    
if(partition.length == 3){
    var dummy:ListBuffer[String] = ListBuffer()
    for(i <- partition){
        dummy+=(i)
    }
    res += dummy
}
else{
    if(start < empSale.size){
        for(i <- start until empSale.size){
        partition.addOne(empSale(i))
        backTrack(empSale,res,partition,i+1,end)
        partition.remove(partition.size - 1)
        }
    }
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
   
    println("Enter the Department Id Employee details you want to view ")
    println("The list of Departments are : Engineering-1, Operations-2 , Sales-3 , Marketing-4 , HR-5 , Finance-6")
    var Filterdepartment = scala.io.StdIn.readInt() 
    var con:Connection = jdbcConnection
    getRowsOfDepartment(Filterdepartment,con)

    var resBuffer:ListBuffer[ListBuffer[String]] = ListBuffer()
    backTrack(empSales,resBuffer,ListBuffer(),0,3)
    resBuffer.foreach(println)
    
} 
```



#### Error stacktrace:

```

```
#### Short summary: 

dotty.tools.dotc.core.TypeError$$anon$1: Toplevel definition filterDep is defined in
  <WORKSPACE>/Day-5/ListCsvEmpDB.scala
and also in
  <WORKSPACE>/Day-6/ListCsvEmpDBThread.scala
One of these files should be removed from the classpath.