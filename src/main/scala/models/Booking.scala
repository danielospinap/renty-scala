package models

import java.util.Date

import io.circe.syntax._
import io.circe._
import org.bson.types.ObjectId

case class Booking(
                    _id: ObjectId,
                    carId: ObjectId,
                    userId: ObjectId,
                    from: Date,
                    to: Date,
                    pickUp: String
                   )

case class createBooking(
                          carId: String,
                          userId: String,
                          from: String,
                          to: String,
                          pickUp: String
                        )

case class Bookings(list: Seq[Booking])


object Booking {
  implicit val encoder: Encoder[Booking] = (booking: Booking) => {
    Json.obj(
      "id" -> booking._id.toHexString.asJson,
      "carId" -> booking.carId.toHexString.asJson,
      "userId" -> booking.userId.toHexString.asJson,
      "from" -> booking.from.toString.asJson,
      "to" -> booking.from.toString.asJson,
      "pickUp" -> booking.pickUp.asJson
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