file://<WORKSPACE>/Database.scala
### dotty.tools.dotc.core.TypeError$$anon$1: Toplevel definition departmentList is defined in
  <WORKSPACE>/Day-5/ListCsvEmp.scala
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
offset: 276
uri: file://<WORKSPACE>/Database.scala
text:
```scala
import java.sql.{Connection, DriverManager, ResultSet, Statement}

object DatabaseExample {
  def main(args: Array[String]): Unit = {
    // Load the JDBC driver
    Class.forName("com.mysql.cj.jdbc.Driver")

    // Establish a connection
    val url = "jdbc:mysql://hadoop-se@@rver.mysql.database.azure.com:3306/sreeja"
    val username = "sqladmin"
    val password = "Password@12345"
    val connection: Connection = DriverManager.getConnection(url, username, password)

    try {
      // Create a statement
      val statement: Statement = connection.createStatement()

      // Create a table
      val createTableSQL =
        """
CREATE TABLE IF NOT EXISTS employees (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100),
  age INT
)
""".stripMargin

      statement.execute(createTableSQL)
      println("Table created successfully.")

      // Insert some data
      val insertSQL =
        """
INSERT INTO employees (name, age)
VALUES ('John Doe', 30),
       ('Jane Smith', 25)
""".stripMargin

      statement.executeUpdate(insertSQL)
      println("Data inserted successfully.")

      // Query the data
      val query = "SELECT * FROM employees"
      val resultSet: ResultSet = statement.executeQuery(query)

      // Process the ResultSet
      println("Employees:")
      while (resultSet.next()) {
        val id = resultSet.getInt("id")
        val name = resultSet.getString("name")
        val age = resultSet.getInt("age")
        println(s"ID: $id, Name: $name, Age: $age")
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally {
      // Close Statement and Connection
      connection.close()
    }
  }
}
```



#### Error stacktrace:

```

```
#### Short summary: 

dotty.tools.dotc.core.TypeError$$anon$1: Toplevel definition departmentList is defined in
  <WORKSPACE>/Day-5/ListCsvEmp.scala
and also in
  <WORKSPACE>/Day-5/ListCsvEmpDB.scala
One of these files should be removed from the classpath.