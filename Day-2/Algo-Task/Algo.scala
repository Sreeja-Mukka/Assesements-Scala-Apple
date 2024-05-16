import scala.util.Random


def printArray(nums: Array[Int]) : Unit = {
    for (i <- 0 until nums.length) {
        print(nums(i) + " ")
    }
    println()
}


def AlgoSearch(name: String) : (Array[Int])=>Unit = {
    if(name == "bubble"){
        BubbleSort
    }
    else if (name == "insertion"){
        InsertionSort
    }
    else if (name == "quick") {
        QuickSort
    }
    else if (name == "merge") {
        MergeSort
    }
    else if (name == "radix") {
        RadixSort
    }
    else if (name == "binary") {
        BinarySearch
    }
    else {
        (x) => println("nothing found")
    }
}

def BubbleSort(arr: Array[Int]) :Unit = {
    var nums = arr
    for(i <- 0 until arr.length-1)
        for(j <- 0 until arr.length-i-1) 
            if(nums(j) > nums(j+1)) 
                val temp = nums(j)
                nums(j) = nums(j+1)
                nums(j+1) = temp

    println("The Number Array after sorting")
    printArray(nums)
}

def InsertionSort(arr: Array[Int]) :Unit = {

    var nums = arr
    var j=0
    for(i <- 0 until arr.length)
        var item = nums(i)
        j=i-1
        while(j>=0 && nums(i)>item){
            nums(j+1) = nums(j)
            j=j-1
        }
        nums(j+1) = item
    println("The Number Array after sorting")
    printArray(nums)   
    
}

def QuickSort(arr: Array[Int]) :Unit = {
    var nums = arr

    def swap(nums: Array[Int],i: Int, j: Int) : Unit={
       var temp = nums(i);
       nums(i) = nums(j);
       nums(j) = temp;
    }

    def partition(nums: Array[Int],low:Int,high:Int): Int={
        val pivot = nums(high)
        var i = (low-1)

        for(j <- low to high-1){
            if(nums(j)<pivot){
                i=i+1
                swap(nums,i,j)
            }
        }
        swap(nums,i+1,high)

        return i+1
    }

    def quickSort(nums: Array[Int],l: Int, r: Int) : Unit = {

     if(l<r){
        val pi = partition(nums,l,r)

        quickSort(nums,l,pi-1)
        quickSort(nums,pi+1,r)
     }
    }

    quickSort(nums,0, arr.length - 1)
    
    println("The Number Array after sorting")
    printArray(nums) 
}

def MergeSort(arr: Array[Int]) :Unit = {
    var nums = arr
    def merge(nums:Array[Int],l:Int,m:Int,r:Int) : Unit = {
        val n1 = m-l+1
        val n2 = r-m

        var lArray = new Array[Int](n1)
        var rArray = new Array[Int](n2)

        for(x <- 0 until n1){
            lArray(x) = nums(l+x)
        }
        for(y <- 0 until n2){
            rArray(y) = nums(m+1+y)
        }

        var i=0
        var j=0
        var k = l

        while( i < n1 && j < n2) {
            if(lArray(i) <= rArray(j)){
                nums(k) = lArray(i)
                i+=1
            }
            else{
                nums(k) = rArray(j)
                j+=1
            }
            k+=1
        }

        while(i<n1){
            nums(k) = lArray(i)
            i+=1
            k+=1
        }
        while(j<n2){
            nums(k) = rArray(j)
            j+=1
            k+=1
        }
    }
    def sort(nums:Array[Int],l:Int,r:Int): Unit={
        if(l<r){
            val m = l+(r-l)/2

            sort(nums,l,m)
            sort(nums,m+1,r)

            merge(nums,l,m,r)
        }
    }
    sort(nums,0,nums.length-1)

    println("The Number Array after sorting")
    printArray(nums) 
}

def RadixSort(arr: Array[Int]) :Unit = {
    var nums = arr
    def getMax(nums:Array[Int],n:Int) : Int = {
        var maxi = nums(0)
        for(i <- 1 until n){
            if(nums(i)>maxi)
            maxi=nums(i)
        }
        maxi
    }

    def countSort(nums: Array[Int],n:Int,exp:Int): Unit = {
        var op = new Array[Int](n)
        var i=0
        var count = new Array[Int](10)

        for(i <- 0 until n){
            count( (nums(i) / exp) % 10 ) +=1
        }
        for(i <- 1 until 10){
            count(i)+= count(i-1)
        }
        i=n-1
        while(i>=0){
            op(count (nums(i)/exp)%10 - 1 ) = nums(i)
            count( (nums(i) / exp ) % 10 ) -=1
            i-=1
        }

        for(i <- 0 until n){
            nums(i) = op(i)
        }
    }
    def sort(nums: Array[Int],n:Int): Unit = {
        val m = getMax(nums,n)

        var exp = 1
        while (m / exp > 0) {
            countSort(nums,n,exp)
            exp *= 10
        }
    }
    sort(nums,nums.length)

    println("The Number Array after sorting")
    printArray(nums) 
    }

def BinarySearch(arr: Array[Int]) :Unit = {
    println("Enter the element to be searched for Binary Search")
    var ele = scala.io.StdIn.readInt()
    var f=0
    var index = 0
   
    for(i <- 0 until arr.length) {
        if(arr(i) == ele){
            f=1
            index = i
        }
    }
    if(f==1){
          println(s"Number $ele found at position $index")
    }
    else{
        println(s"Number $ele not found ")
    }
}


@main def ChooseSort = {
    val nums = Array(2,4,5,1,8)

    println("The Array before Sorting ")
    printArray(nums)

    println("------")
    println("Using Bubble Sort")
    val bubble = AlgoSearch("bubble")
    bubble(nums)
    println("------")
    println("Using Insertion Sort")
    val insertion = AlgoSearch("insertion")
    insertion(nums)
    println("------")
    println("Using Quick Sort")
    val quick = AlgoSearch("quick")
    quick(nums)
    println("------")
    println("Using Merge Sort")
    val merge = AlgoSearch("merge")
    merge(nums)
    println("------")
    println("Using Radix Sort")
    val radix = AlgoSearch("radix")
    radix(nums)
    println("------")
    println()
    val binary = AlgoSearch("binary")
    binary(nums)

}