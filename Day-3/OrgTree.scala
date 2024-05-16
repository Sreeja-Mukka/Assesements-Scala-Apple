// org parent
// - finance -[ payments  , sales ] c1

// payment class - [ tuples ] c1 cc1

// sales - [marketing , adv , salesMang] c1cc2
// marketing - [tuples] c1cc21
// adv - [tuples] c1cc22 
// salesMang - [tuples] c1cc23
// sales - [tuples ]
import scala.collection.mutable.ArrayBuffer
class Employee (sno:Int,name:String,city:String,department:String){
    override def toString(): String = s"($sno,$name,$city)"
}

var payments = ArrayBuffer[Employee]()
var sales = ArrayBuffer[Employee]()
var marketing = ArrayBuffer[Employee]()
var Adver = ArrayBuffer[Employee]()
var salesManag = ArrayBuffer[Employee]()


def printArrayEmp():Unit = {
    println("Organisation")
    println("     |____ Finance")
    println("           |___Payments")
    if(payments.size > 0){
        for(ele <- payments) {
            println("           |       |---"+" "+ele.toString())
        }
    }

    println("           |____ Sales")
    println("                  |")
    println("                  "+"|____ Marketing")
    if(marketing.size > 0) {
        for(ele <- marketing) {
            println("                  |    |___"+" "+ele.toString())
        }
    }
    
    if(sales.size > 0){
        for(ele <- sales) {
            println("                  "+"|____"+ele.toString())
        }
    }
    
    println("                  "+"|____ Advertisements")
    if(Adver.size > 0){
        for(ele <- Adver) {
            println("                  |    |___"+" "+ele.toString())
        }
    }

    println("                  "+"|____ SalesManagement")

    if(salesManag.size > 0){
        for(ele <- Adver) {
            println("                  |    |___"+" "+ele.toString())
        }
    }
}

@main def OrgTreeMain = {
    // enters name , sno , city , dep
    
    var k=0
    println("Enter the Employee data to be added in their department")
    while(k!=1) {
   
    println("Do you want to enter the details of the Employees? Press 1 to enter , to Exit Enter 0")
    val flag = scala.io.StdIn.readInt()
    if(flag==1) {
        println("Departments present in the Organisation are, payments , sales , marketing , advertisements , salesManagement ")
        println("please enter the sno,name,city,department")
    
        var sno = scala.io.StdIn.readInt()
        var name = scala.io.StdIn.readLine() 
        var city = scala.io.StdIn.readLine() 
        var department = scala.io.StdIn.readLine() 
        var empTuple:Employee = new Employee(sno,name,city,department)

        if(department == "payments") {
            payments+=empTuple  
        }
        else if(department == "sales") {
            sales+=empTuple 
        }
        else if(department == "marketing") {
            marketing+=empTuple
        }
        else if(department == "advertisements"){
            Adver+=empTuple
        }
        else if(department == "salesManagement") {
            salesManag+=empTuple
        }
        else{
            println("The Entered Department is not in the organisation, enter the correct Department")
        }
        printArrayEmp()
    }
    else{
        k=1
    }
}

}