file://<WORKSPACE>/Day-4/ListCsvEmp.scala
### java.lang.AssertionError: NoDenotation.owner

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 3.3.3
Classpath:
<HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala3-library_3/3.3.3/scala3-library_3-3.3.3.jar [exists ], <HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.12/scala-library-2.13.12.jar [exists ]
Options:



action parameters:
offset: 246
uri: file://<WORKSPACE>/Day-4/ListCsvEmp.scala
text:
```scala
import scala.io.Source
import scala.collection.mutable.ListBuffer


case class Employee(sno:Int , ename:String , ecity:String , esal: Int , edepartment:String)

case class Department(dno:Int , dname:String)

object DepartmentTree { 
    var List[@@]
    def addDepartment : Unit = {

    }
}
val filePath = "EmpData.csv"

def readFile(fileName:String ) : List[String] = {
    val EmpDataSource = Source.fromFile(filePath)
    val lines = EmpDataSource.getLines().toList

    EmpDataSource.close()
    lines
}

def filterData(EmpData:ListBuffer[Employee],fsal:Int , fdep:String) : ListBuffer[Employee] = {
    var FilterdEmpData = EmpData.filter(emp => emp.esal > fsal && emp.edepartment == fdep)
    FilterdEmpData
}

def MapData(EmpData:ListBuffer[Employee]) : ListBuffer[String] = {
    var MapEmpData = EmpData.map(emp => s"The Employee with name ${emp.ename} holding the employee number ${emp.sno} with salary ${emp.esal} belongs to ${emp.edepartment}.")
    MapEmpData
}

def ReduceEmpData(Empdata:ListBuffer[Employee]) = {
    val groupByDep = Empdata.groupBy(_.edepartment)

    val reducedData = groupByDep.mapValues { emp=>
        val totalSal = emp.map(_.esal).sum 
        val avgSal = totalSal / emp.length
        val empCount = emp.length
        (totalSal , avgSal , empCount )
    }

    reducedData.foreach { case (department , (totalSal , avgSal , empCount)) =>
        println(s"Department: $department")
        println(s"Total Salaries : $totalSal")
        println(s"Average Salaries : $avgSal")
        println(s"Number of Emp of That Department : $empCount")
        println()
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
    //println(EmpData)
    println("Please enter the salary and the department which you want to filer the Employee data")
    var Fsal = scala.io.StdIn.readInt()
    var Fdep = scala.io.StdIn.readLine() 

    println()
    println("The Filtered Data with resp to the Department and Salary are: ")
    
    var FilteredEmpList = filterData(EmpData,Fsal,Fdep)
    FilteredEmpList.foreach(println)
    println()

    println("The Mapped Data Format: ")
    println()

    var MappedData = MapData(EmpData)
    for(data <- MappedData) {
        println(data)
        println()
    }

    ReduceEmpData(EmpData)
} 
```



#### Error stacktrace:

```
dotty.tools.dotc.core.SymDenotations$NoDenotation$.owner(SymDenotations.scala:2607)
	scala.meta.internal.pc.SignatureHelpProvider$.isValid(SignatureHelpProvider.scala:83)
	scala.meta.internal.pc.SignatureHelpProvider$.notCurrentApply(SignatureHelpProvider.scala:96)
	scala.meta.internal.pc.SignatureHelpProvider$.$anonfun$1(SignatureHelpProvider.scala:48)
	scala.collection.StrictOptimizedLinearSeqOps.dropWhile(LinearSeq.scala:280)
	scala.collection.StrictOptimizedLinearSeqOps.dropWhile$(LinearSeq.scala:278)
	scala.collection.immutable.List.dropWhile(List.scala:79)
	scala.meta.internal.pc.SignatureHelpProvider$.signatureHelp(SignatureHelpProvider.scala:48)
	scala.meta.internal.pc.ScalaPresentationCompiler.signatureHelp$$anonfun$1(ScalaPresentationCompiler.scala:414)
```
#### Short summary: 

java.lang.AssertionError: NoDenotation.owner