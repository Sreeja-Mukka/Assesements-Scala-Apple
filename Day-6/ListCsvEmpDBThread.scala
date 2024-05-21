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
 
val depInfo = HashMap[String,Int]()

def DepMap(depList:ListBuffer[String]) = {
  val con:Connection  = jdbcConnection

  for(dep <- depList) {
      val depIdQuery = con.prepareStatement("select id from department where dep_name = ?")
      depIdQuery.setString(1,dep)
      val res = depIdQuery.executeQuery()
                
      while(res.next()) {
        val id = res.getInt("id")
        depInfo.put(dep,id)    
      }
  }
  con.close()

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
  statement.setInt(7,depInfo.get(emp.edepartment).getOrElse(-1))
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
    DepMap(departmentList)
    //println(departmentList.indexOf(""))
    addEmpDataThread(EmpData)
    
} 