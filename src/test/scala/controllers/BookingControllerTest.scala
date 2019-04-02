package controllers

import java.io.InputStream
import java.net.InetSocketAddress
import java.nio.file.{Files, Path, Paths}

import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{HttpEntity, MediaTypes, MessageEntity, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.util.ByteString
import com.firebase4s.App
import de.bwaldvogel.mongo.MongoServer
import de.bwaldvogel.mongo.backend.memory.MemoryBackend
import io.circe.Json
import models._
import models.repository.{BookingRepositoryMongo, CarRepositoryMongo}
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.bson.codecs.configuration.CodecRegistry
import org.bson.types.ObjectId
import org.mongodb.scala.{MongoClient, MongoCollection, ServerAddress}
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import org.scalatest.AsyncFunSuite

class BookingControllerTest extends AsyncFunSuite with ScalatestRouteTest {

  val server: MongoServer = new MongoServer(new MemoryBackend)
  val socketAddres: InetSocketAddress = server.bind()
  val severAddress: ServerAddress = new ServerAddress(socketAddres)
  val client: MongoClient = MongoClient("mongodb://" + severAddress.toString)
  val codecRegistry: CodecRegistry = fromRegistries(fromProviders(classOf[Car], classOf[CreateCar], classOf[Rental], classOf[Booking]), DEFAULT_CODEC_REGISTRY)
  val carCollection: MongoCollection[Car] = client.getDatabase("testDB").withCodecRegistry(codecRegistry).getCollection[Car]("cars")
  val carRepository: CarRepositoryMongo = new CarRepositoryMongo(carCollection)
  val bookingCollection: MongoCollection[Booking] = client.getDatabase("testDB").withCodecRegistry(codecRegistry).getCollection[Booking]("bookings")
  val bookingRepository: BookingRepositoryMongo = new BookingRepositoryMongo(bookingCollection)

  val path: Path = Paths.get("src/main/resources/serviceAccountCredentials.json")
  val is: InputStream = Files.newInputStream(path)

  App.initialize(is,"https://renty-vue.firebaseio.com/")

  val bookingController = new BookingController(bookingRepository, carRepository)

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

  val createdBooking = new CreateBooking(
    "user1",
    car1,
    "05/03/20019",
    "aeropuerto",
    "07/03/2019",
    "aeropuerto",
    "20/03/2019",
    rental
  )

  test("return error in create booking with wrong data") {
    val jsonRequest = ByteString(
      s"""
         |{
         |  "token": "token123",
         |	"bookingDate": "01/01/2000",
         |	"pickupDate": "03/01/2000",
         |	"deliverDate": "15/01/2000",
         |	"pickup": "aeropuerto",
         |	"deliverPlace": "xxxxx",
         |	"carId": "123"
         |}
       """.stripMargin)
    val entity = HttpEntity(MediaTypes.`application/json`, jsonRequest)
    carCollection.insertOne(car1).toFuture() map { _ =>
      Post("/booking").withEntity(entity) ~> bookingController.route ~> check {
        println(s"\n\n\n$status")
        assert(status.equals(StatusCodes.InternalServerError))
      }
    }
  }

  test("return error in delete non existent booking") {
    Delete("/booking/delete/?bookingId=23456") ~> bookingController.route ~> check {
      assert(status.equals(StatusCodes.InternalServerError))
    }
  }

  test("delete a created booking") {
    bookingRepository.save(createdBooking) map { id =>
      println(id)
      Delete(s"/booking/delete/?bookingId=$id") ~> bookingController.route ~> check {
        assert(status.equals(StatusCodes.OK))
      }
    }
  }
}
