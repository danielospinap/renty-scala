package models.repository

import java.net.InetSocketAddress
import java.text.SimpleDateFormat

import de.bwaldvogel.mongo.MongoServer
import de.bwaldvogel.mongo.backend.memory.MemoryBackend
import models._
import org.bson.codecs.configuration.CodecRegistries._
import org.bson.types.ObjectId
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.{MongoClient, ServerAddress}
import org.scalatest.FunSuite
import scala.util.Success

import scala.concurrent.ExecutionContext.Implicits.global

class BookingRepositoryTest extends FunSuite {

  val server = new MongoServer(new MemoryBackend)
  val severAddress: InetSocketAddress = server.bind()
  val saddress = new ServerAddress(severAddress)
  val client = MongoClient("mongodb://" + saddress.toString)
  val codecRegistry = fromRegistries(fromProviders(classOf[Car], classOf[CreateCar], classOf[Rental], classOf[Booking]), DEFAULT_CODEC_REGISTRY)
  val bookingCollection = client.getDatabase("testDB").withCodecRegistry(codecRegistry).getCollection[Booking]("bookings")
  val bookingRepository = new BookingRepositoryMongo(bookingCollection)

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

  val car2: Car = new Car(
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

  val booking1 = new Booking(
    ObjectId.get(),
    "user1",
    car1,
    new SimpleDateFormat("dd/MM/yyyy").parse("05/03/20019"),
    "aeropuerto",
    new SimpleDateFormat("dd/MM/yyyy").parse("07/03/20019"),
    "aeropuerto",
    new SimpleDateFormat("dd/MM/yyyy").parse("20/03/20019"),
    rental
  )

  val booking2 = new Booking(
    ObjectId.get(),
    "user2",
    car1,
    new SimpleDateFormat("dd/MM/yyyy").parse("11/04/20019"),
    "aeropuerto",
    new SimpleDateFormat("dd/MM/yyyy").parse("15/04/20019"),
    "aeropuerto",
    new SimpleDateFormat("dd/MM/yyyy").parse("30/04/20019"),
    rental
  )

  val booking3 = new Booking(
    ObjectId.get(),
    "user1",
    car2,
    new SimpleDateFormat("dd/MM/yyyy").parse("05/03/20019"),
    "aeropuerto",
    new SimpleDateFormat("dd/MM/yyyy").parse("07/03/20019"),
    "aeropuerto",
    new SimpleDateFormat("dd/MM/yyyy").parse("20/03/20019"),
    rental
  )

  test("find one booking by id") {
    bookingCollection.insertMany(Seq(booking1, booking2, booking3)).toFuture() onComplete {
      case Success(_) =>
        bookingRepository.findById(booking1._id.toHexString) onComplete {
          case Success(booking) =>
            assert(booking.equals(booking1))
        }
    }
  }

  test("save one booking") {
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

    bookingRepository.save(createdBooking) onComplete {
      case Success(_) => {

      }
    }
  }

}
