package controllers

import java.net.InetSocketAddress

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import de.bwaldvogel.mongo.MongoServer
import org.bson.codecs.configuration.CodecRegistries._
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import de.bwaldvogel.mongo.backend.memory.MemoryBackend
import models.repository.{BookingRepositoryMongo, CarRepository, CarRepositoryMongo}
import models.{Booking, Car, CreateCar, Rental}
import org.bson.codecs.configuration.CodecRegistry
import org.bson.types.ObjectId
import org.mongodb.scala.{Completed, MongoClient, MongoCollection, ServerAddress}
import org.scalatest.{AsyncFunSuite, FunSuite}

import scala.concurrent.Future
import scala.util.Success
import scala.util.Failure

class CarControllerTest extends AsyncFunSuite with ScalatestRouteTest {

  import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
  import io.circe.generic.auto._

  val server: MongoServer = new MongoServer(new MemoryBackend)
  val socketAddres: InetSocketAddress = server.bind()
  val severAddress: ServerAddress = new ServerAddress(socketAddres)
  val client: MongoClient = MongoClient("mongodb://" + severAddress.toString)
  val codecRegistry: CodecRegistry = fromRegistries(fromProviders(classOf[Car], classOf[CreateCar], classOf[Rental], classOf[Booking]), DEFAULT_CODEC_REGISTRY)
  val carCollection: MongoCollection[Car] = client.getDatabase("testDB").withCodecRegistry(codecRegistry).getCollection[Car]("cars")
  val carRepository: CarRepositoryMongo = new CarRepositoryMongo(carCollection)
  val bookingCollection: MongoCollection[Booking] = client.getDatabase("testDB").withCodecRegistry(codecRegistry).getCollection[Booking]("bookings")
  val bookingRepository: BookingRepositoryMongo = new BookingRepositoryMongo(bookingCollection)

  val carController = new CarController(carRepository, bookingRepository)

  val pictures: List[String] = List("a", "b", "c")

  val rental = new Rental(
    123,
    "rental"
  )

  val car1: Car = new Car(
    ObjectId.get(),
    "brand",
    "thumbnail",
    "999",
    "type",
    "model",
    "plate",
    5,
    4,
    "transmission",
    4,
    "red",
    100,
    pictures,
    rental
  )

  test("return error with wrong id") {

    Get("/cars/12345") ~> carController.route ~> check {
      assert(status.equals(StatusCodes.InternalServerError))
    }
  }

  test("return car") {
    carCollection.insertOne(car1).toFuture() map { _ =>
      Get(s"/cars/${car1._id.toHexString}") ~> carController.route ~> check {
        assert(status.equals(StatusCodes.OK))
      }
    }
  }

  test("return cars with valid date") {
    Get("/cars/search/?from=05/01/2000&to=14/01/2000&type=Automovil&pickup=aeropuerto") ~> carController.route ~> check {
      assert(status.equals(StatusCodes.OK))
    }
  }

  test("return error with wrong date") {
    Get("/cars/search/?from=05-01-2000&to=14/01/2000&type=Automovil&pickup=aeropuerto") ~> carController.route ~> check {
      assert(status.equals(StatusCodes.InternalServerError))
    }
  }
}
