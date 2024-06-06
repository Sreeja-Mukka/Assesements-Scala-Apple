val numbers = List(1, 2, 3, 4)

// Multiply each element by 2 using underscore
val doubled = numbers.map(_ * 2)
@main def underscoreMain = {
println(doubled.foreach(println))  
}
