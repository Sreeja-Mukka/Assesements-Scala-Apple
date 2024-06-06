// def log(level: String, message: String): Unit = {
//   println(s"[$level] $message")
// }

// // Partially apply `log` to create a debug log function
// val debugLog = log("DEBUG", _: String)

import java.util.Date
import java.text.SimpleDateFormat

def formatDate(format: String, date: Date): String = {
  val dateFormat = new SimpleDateFormat(format)
  dateFormat.format(date)
}

// Partially apply `formatDate` to use a specific format
val formatAsIso = formatDate("yyyy-MM-dd", _: Date)


@main def mainApp = {
// Usage
println(formatAsIso(new Date()))
//debugLog("This is a debug message.")
}
