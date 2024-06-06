// A simple service that requires some configuration
case class ServiceConfig(port: Int)

// A function that performs some operation requiring configuration
def startService()(implicit config: ServiceConfig): Unit = {
  println(s"Starting service on port ${config.port}")
}

// Define an implicit value that will be passed automatically
implicit val defaultConfig: ServiceConfig = ServiceConfig(8080)
@main def impiliScala = {
// Usage
startService()  

/* So here , first the call goes to the main method, where the function of that type is an implicit type , which we need to intialise or define the value of it. */

}// Outputs: Starting service on port 8080
