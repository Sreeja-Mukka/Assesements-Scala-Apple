val squareRoot: PartialFunction[Int, String] = { // this is the method format [Input, Output]
  case x if x > 0 => Math.sqrt(x)+" square root"
}

@main def partialMain = {
// Usage
if (squareRoot.isDefinedAt(4)) {
  println(squareRoot(4))  // Output: 2.0
}
if (squareRoot.isDefinedAt(-1)) {
  println("Not defined for negative numbers.")
} else {
  println("Input must be a positive integer.")
}
}