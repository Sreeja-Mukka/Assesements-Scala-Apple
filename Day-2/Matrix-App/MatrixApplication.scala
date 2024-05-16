def printArray(nums: Array[Array[Int]]) : Unit = {
    for (i <- 0 until 10) {
      for (j <- 0 until 10) {
        print(nums(i)(j) + "  ")
      }
      println()
    }
}

def allocateSeat(nums: Array[Array[Int]] ,seatNo : Int) :Array[Array[Int]] = {
    val list = List(1, 2,3,4,5,6,7,8,9)
    var f = 0
    val x = list.contains(seatNo)
    if (x==true)
        nums(0)(seatNo-1) = 0
    else {
        var seatNumber = seatNo
        var row = seatNumber/10
        var col = seatNumber%10 - 1

        // println("row and col")
        // println(row)
        // println(col)
        if(row >=0 && col >=0){
            if(nums(row)(col)!=0){
            nums(row)(col) = 0
            f=1
            }
        }
        if (col < 0 && row > 0) {
            if(nums(row-1)(9)!=0){
            nums(row-1)(9) = 0
            f=1
            }
        }
        if(f==1){
            println(s"Booked the Seat $seatNo")
        }
        else{
            println(s"Already Allocated this $seatNo , choose another seat")
        }
    }
    return nums
}

@main def MainMethod = {
    var nums: Array[Array[Int]] = Array.ofDim(10,10)
    var value = 1
    // init the array
    for(i <- 0 until 10 ;j <- 0 until 10){
        nums(i)(j) = value
        value+=1
    }
    println("Welcome to booking Application , please select your seat")
    printArray(nums)
    println("-------------------")

    var flag = 1
    while(flag==1) {
    println("choose the seat accordingly or enter -1 to exit")
    val bookingSeat = scala.io.StdIn.readInt()
    if(bookingSeat < 0){
        println("Thank you for using the APPLICATION , see you soon!!")
        flag=0
    }
    else{
        nums = allocateSeat(nums,bookingSeat)
        printArray(nums)
        println("-------------------")
    }
    
    // nums = allocateSeat(nums,35)
    // printArray(nums)
    // println("-------------------")
    // nums = allocateSeat(nums,91)
    // printArray(nums)
    // println("-------------------")
    // nums = allocateSeat(nums,91)
    // printArray(nums)
    // println("-------------------")
    
    }
}