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
    val statement = con.prepareStatement("INSERT INTO department (id,dep_name) VALUES (?,?)")
    try {
        var n=1
      // Create a statement
      //val statement: Statement = con.createStatement()
        for(dep <- depList) {
        //println(dep)
        statement.setInt(1,n)
        n+=1
        statement.setString(2,dep)
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

def getRowsOfDepartment(dep_id:Int,con:Connection) : Unit = {
    val statement = con.prepareStatement("select * from employee where dep_id = ?")
    try{
    statement.setInt(1,dep_id) 
    println()
    println(s"The List of Employees in the Department $dep_id are :")
    println()
    val res = statement.executeQuery()
    while(res.next()) 
    {
        val emp_id = res.getInt("emp_id")
        val emp_name = res.getString("emp_name")
        val emp_city = res.getString("city")
        val emp_sal = res.getInt("salary")
        println(s"Id: $emp_id , Name: $emp_name , City: $emp_city , Salary: $emp_sal")
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
    //var con:Connection = jdbcConnection
    //addEmpToDepartment(EmpData,con)
    println("Enter the Department Id Employee details you want to view ")
    println("The list of Departments are : Engineering-1, Operations-2 , Sales-3 , Marketing-4 , HR-5 , Finance-6")
    var Filterdepartment = scala.io.StdIn.readInt() 
    var con:Connection = jdbcConnection
    getRowsOfDepartment(Filterdepartment,con)
} 