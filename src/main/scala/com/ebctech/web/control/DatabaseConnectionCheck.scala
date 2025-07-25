//import slick.jdbc.PostgresProfile.api._
//import scala.concurrent.Await
//import scala.concurrent.duration._
//import scala.util.{Failure, Success}
//import scala.concurrent.ExecutionContext.Implicits.global
//
//object DatabaseConnectionCheck {
//
//  val db = Database.forConfig("database-config.db")
//
//  def checkConnection(): Unit = {
//    val action = sql"SELECT * from flxPoint_orders".as[Int]
//
//    val future = db.run(action)
//
//    future.onComplete {
//      case Success(result) =>
//        println(s"✅ Database connection successful. Result: ${result.headOption.getOrElse("No result")}")
//      case Failure(exception) =>
//        println(s"❌ Failed to connect to the database: ${exception.getMessage}")
//    }
//
//    // Wait just for demonstration purposes
//    Await.result(future, 1.seconds)
//    db.close()
//  }
//
//  def main(args: Array[String]): Unit = {
//    checkConnection()
//  }
//}
