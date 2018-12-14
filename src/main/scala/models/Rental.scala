package models

import io.circe.{Encoder, Json}
import org.bson.types.ObjectId
import io.circe.syntax._

case class Rental(
                   _id: Int,
                   name: String
                 )

object Rental {
  implicit val encoder: Encoder[Rental] = (rental: Rental) => {
    Json.obj(
      "id" -> rental._id.asJson,
      "name" -> rental.name.asJson
    )
  }
}
