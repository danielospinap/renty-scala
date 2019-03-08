import java.io.InputStream
import java.nio.file.{Files, Path, Paths}

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.firebase4s.App
import controllers._
import models.repository.{BookingRepositoryMongo, CarRepositoryMongo}
import mongodb.Mongo

import scala.concurrent.Await
import scala.util.{Failure, Success}

object Main extends App {
  import system.dispatcher

  val host = "0.0.0.0"
  val port = System.getenv("PORT").toInt

  val path: Path = Paths.get("src/main/resources/serviceAccountCredentials.json")
  val is: InputStream = Files.newInputStream(path)

  println(is)

  App.initialize(is,"https://renty-vue.firebaseio.com/")

  implicit val system: ActorSystem = ActorSystem("renty-scala")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val log = system.log

  //Repositories
  val carRepository: CarRepositoryMongo = new CarRepositoryMongo(Mongo.carCollection)
  val bookingRepository: BookingRepositoryMongo = new BookingRepositoryMongo(Mongo.bookingCollection)

  //Controllers
  val routerCars: CarController = new CarController(carRepository, bookingRepository)
  val routerBooking: BookingController = new BookingController(bookingRepository, carRepository)

  import akka.http.scaladsl.server.Directives._
  val server = new Server(routerCars.route ~ routerBooking.route, host, port)

  val binding = server.bind()
  binding.onComplete {
    case Success(_) => log.info(s"Server up and running.")
    case Failure(exception) => log.error(s"Failed: ${exception.getMessage}")
  }

  import scala.concurrent.duration._
  Await.result(binding, 7.seconds)
}
