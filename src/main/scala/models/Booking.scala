package models

import java.util.Date

import io.circe.syntax._
import io.circe._
import org.bson.types.ObjectId

case class Booking(
                    _id: ObjectId,
                    userId: String,
                    car: Car,
                    bookingDate: Date,
                    pickUp: String,
                    pickupDate: Date,
                    deliverPlace: String,
                    deliverDate: Date,
                    rental: Rental
                   )

case class CreateBooking(
                          userId: String,
                          car: Car,
                          bookingDate: String,
                          pickUp: String,
                          pickupDate: String,
                          deliverPlace: String,
                          deliverDate: String,
                          rental: Rental
                        )

case class Bookings(list: Seq[Booking])


object Booking {
  implicit val encoder: Encoder[Booking] = (booking: Booking) => {
    Json.obj(
      "id" -> booking._id.toHexString.asJson,
      "carId" -> booking.car.asJson,
      "userId" -> booking.userId.asJson,
      "bookingDate" -> booking.bookingDate.toString.asJson,
      "pickUp" -> booking.pickUp.asJson,
      "pickupDate" -> booking.pickupDate.toString.asJson,
      "deliverPlace" -> booking.deliverPlace.asJson,
      "deliverDate" -> booking.deliverDate.toString.asJson
    )
  }
}

object Bookings {
  implicit val encoder: Encoder[Bookings] = (bookings: Bookings) => {
    Json.obj(
      "list" -> bookings.list.asJson
    )
  }
}