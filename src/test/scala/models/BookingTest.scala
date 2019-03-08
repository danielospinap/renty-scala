package models

import java.text.SimpleDateFormat
import java.util.Date

import io.circe.Json
import org.bson.types.ObjectId
import org.scalatest.FunSuite
import io.circe.syntax._

class BookingTest extends FunSuite{

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

  val booking1: Booking = new Booking(
    ObjectId.get(),
    "userid",
    car1,
    new SimpleDateFormat("dd/MM/yyyy").parse("05/05/2005"),
    "aeropuerto",
    new SimpleDateFormat("dd/MM/yyyy").parse("06/05/2005"),
    "aeropuerto",
    new SimpleDateFormat("dd/MM/yyyy").parse("11/05/2005"),
    rental
  )

  val booking2: Booking = new Booking(
    ObjectId.get(),
    "userid",
    car1,
    new SimpleDateFormat("dd/MM/yyyy").parse("05/05/2005"),
    "aeropuerto",
    new SimpleDateFormat("dd/MM/yyyy").parse("06/05/2005"),
    "aeropuerto",
    new SimpleDateFormat("dd/MM/yyyy").parse("11/05/2005"),
    rental
  )

  val booking3: Booking = new Booking(
    ObjectId.get(),
    "userid",
    car1,
    new SimpleDateFormat("dd/MM/yyyy").parse("05/05/2005"),
    "aeropuerto",
    new SimpleDateFormat("dd/MM/yyyy").parse("06/05/2005"),
    "aeropuerto",
    new SimpleDateFormat("dd/MM/yyyy").parse("11/05/2005"),
    rental
  )

  test("Encode booking") {

    val bookingDate1: Date = booking1.bookingDate
    val pickUpDate1: Date = booking1.pickupDate
    val deliverDate1: Date = booking1.deliverDate

    val expectedEncodedBooking: Json = Json.obj(
      "id" -> booking1._id.toHexString.asJson,
      "carId" -> booking1.car.asJson,
      "userId" -> booking1.userId.asJson,
      "bookingDate" -> bookingDate1.toString.asJson,
      "pickUp" -> booking1.pickUp.asJson,
      "pickupDate" -> pickUpDate1.toString.asJson,
      "deliverPlace" -> booking1.deliverPlace.asJson,
      "deliverDate" -> deliverDate1.toString.asJson
    )

    val encodedBooking = Booking.encoder(booking1)

    assert(encodedBooking.equals(expectedEncodedBooking))
  }

  test("Encode bookings") {

    val bookingDate1: Date = booking1.bookingDate
    val pickUpDate1: Date = booking1.pickupDate
    val deliverDate1: Date = booking1.deliverDate

    val bookingDate2: Date = booking2.bookingDate
    val pickUpDate2: Date = booking2.pickupDate
    val deliverDate2: Date = booking2.deliverDate

    val bookingDate3: Date = booking3.bookingDate
    val pickUpDate3: Date = booking3.pickupDate
    val deliverDate3: Date = booking3.deliverDate
  }

  val expectedEncodedBookings: Json = Json.obj(
    "list" -> Seq(booking1, booking2,booking3).asJson
  )

  val bookingList: Bookings = new Bookings(Seq(booking1, booking2, booking3))

  val encodedBookings = Bookings.encoder(bookingList)

  assert(encodedBookings.equals(expectedEncodedBookings))
}
