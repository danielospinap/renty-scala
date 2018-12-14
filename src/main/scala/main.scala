import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

import scala.concurrent.{Await, ExecutionContext}
import scala.util.{Failure, Success}
import controllers._
import models.repository.{BookingRepository, BookingRepositoryMongo, CarRepository, CarRepositoryMongo}
import mongodb.Mongo
import com.firebase4s.App

object Main extends App {

  val host = "0.0.0.0"
  val port = System.getenv("PORT").toInt

  val serviceAccount = getClass.getResourceAsStream("/serviceAccountCredentials.json")
  App.initialize(serviceAccount,"")

  implicit val system: ActorSystem = ActorSystem("renty-scala")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  import system.dispatcher

  val log = system.log

  //Repositories
  val carRepository = new CarRepositoryMongo(Mongo.carCollection)
  val bookingRepository = new BookingRepositoryMongo(Mongo.bookingCollection)

  //Controllers
  val router = new CarController(carRepository, bookingRepository)


  val server = new Server(router, host, port)


  val binding = server.bind()
  binding.onComplete {
    case Success(_) => log.info(s"Server up and running.")
    case Failure(exception) => log.error(s"Failed: ${exception.getMessage}")
  }


  import scala.concurrent.duration._
  Await.result(binding, 7.seconds)
}
