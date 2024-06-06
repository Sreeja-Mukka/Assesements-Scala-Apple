// Suppose we have a class which wraps an integer
class RichInteger(n: Int) {
  def isEven: Boolean = n % 2 == 0
  def isOdd: Boolean = !isEven
  
}
class makeInt(n:Double){  
    def double: Int= n.toInt
}

@main def implicitMain = {
// // Define an implicit conversion from Int to RichInteger
implicit def intToRichInteger(x: Int): RichInteger = new RichInteger(x)
implicit def doubletoInt(x:Double) : makeInt =  new makeInt(x)
val x = 10
println(intToRichInteger(x).isEven)

println(doubletoInt(22.60).double)

}