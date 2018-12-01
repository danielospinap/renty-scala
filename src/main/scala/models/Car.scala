package models

import com.mongodb.util.JSON
import io.circe.syntax._
import io.circe._
import org.bson.types
import org.bson.types.ObjectId

case class Car(
                _id: ObjectId,
                brand: String,
                thumbnail: String,
                price: String,
                carType: String,
                model: String,
                plate: String,
                rating: Int,
                capacity: Int,
                transmission: String,
                doors: Int,
                color: String,
                kms: Int,
                pictures: List[String]
              )

case class CreateCar(
                      brand: String,
                      thumbnail: String,
                      price: String,
                      carType: String,
                      model: String,
                      plate: String,
                      rating: Int,
                      capacity: Int,
                      transmission: String,
                      doors: Int,
                      color: String,
                      kms: Int,
                      pictures: List[String]
                    )

case class Cars(list: Seq[Car])

object Cars {
  //Datos que devuelve para el get
  implicit val encoder: Encoder[Cars] = (myCars: Cars) => {
    Json.obj(
      "list" -> myCars.list.asJson
    )
  }
}


//import com.mongodb.util.JSON
//import io.circe.syntax._
//import io.circe._
//import org.bson.types.ObjectId
//
//case class FindByIdRequest(id: String) {
//  require(ObjectId.isValid(id), "the informed id is not a representation of a valid hex string")
//}
////TODO: Add rental
//case class Car(_id: ObjectId,
//               brand: String,
//               thumbnail: String,
//               price: String,
//               carType: String,
//               model: String,
//               plate: String,
//               rating: Int,
//               capacity: Int,
//               transmission: String,
//               doors: Int,
//               color: String,
//               kms: Int,
//               pictures: List[String],
//               rents: List[String]
//
//              ) {
//  require(brand != null, "Brand not informed")
//  require(thumbnail != null, "Thumbnail not informed")
//  require(price != null, "Price not informed")
//  require(carType != null, "Type not informed")
//  require(model != null, "Model not informed")
//  require(plate != null, "Plate not informed")
//  require(transmission != null, "Transmission not informed")
//  require(color != null, "Color not informed")
//  require(pictures != null, "Pictures not informed")
//
//  require(brand.nonEmpty, "Brand cannot be empty")
//  require(thumbnail.nonEmpty, "Thumbnail cannot be empty")
//  require(price.nonEmpty, "Price cannot be empty")
//  require(carType.nonEmpty, "Type cannot be empty")
//  require(model.nonEmpty, "Model cannot be empty")
//  require(plate.nonEmpty, "Plate cannot be empty")
//  require(transmission.nonEmpty, "Transmission cannot be empty")
//  require(color.nonEmpty, "Color cannot be empty")
//  require(pictures.nonEmpty, "Pictures cannot be empty")
//
//  require(rating > -1, "rating cannot be lower than 0")
//  require(capacity > 0, "capacity cannot be lower than 1")
//  require(doors > 0, "doors cannot be lower than 1")
//  require(kms > -1, "kms cannot be lower than 0")
//
//  //TODO: Add rental
//}
//
object Car {
  //Datos que devuelve para el get
  implicit val encoder: Encoder[Car] = (myCar: Car) => {
    Json.obj(

      //TODO: Add rental
      "id" -> myCar._id.toHexString.asJson,
      "brand" -> myCar.brand.asJson,
      "thumbnail" -> myCar.thumbnail.asJson,
      "price" -> myCar.price.asJson,
      "type" -> myCar.carType.asJson,
      "model" -> myCar.model.asJson,
      "plate" -> myCar.plate.asJson,
      "rating" -> myCar.rating.asJson,
      "capacity" -> myCar.capacity.asJson,
      "transmission" -> myCar.transmission.asJson,
      "doors" -> myCar.doors.asJson,
      "color" -> myCar.color.asJson,
      "kms" -> myCar.kms.asJson,
      "pictures" -> myCar.pictures.asJson
    )
  }

  //Datos que recibe en el post
//  implicit val decoder: Decoder[Car] = (c: HCursor) => {
//    for {
//      brand <- c.downField("brand").as[String]
//      thumbnail <- c.downField("thumbnail").as[String]
//      price <- c.downField("price").as[String]
//      carType <- c.downField("type").as[String]
//      model <- c.downField("model").as[String]
//      plate <- c.downField("plate").as[String]
//      rating <- c.downField("rating").as[Int]
//      capacity <- c.downField("capacity").as[Int]
//      transmission <- c.downField("transmission").as[String]
//      doors <- c.downField("doors").as[Int]
//      color <- c.downField("color").as[String]
//      kms <- c.downField("kms").as[Int]
//      pictures <- c.downField("pictures").as[List[String]]
//      rents <- c.downField("rents").as[List[String]]
//    } yield Car(ObjectId.get(),
//                brand,
//                thumbnail,
//                price,
//                carType,
//                model,
//                plate,
//                rating,
//                capacity,
//                transmission,
//                doors,
//                color,
//                kms,
//                pictures,
//                rents
//    )
//  }
}
//
//case class Message(message: String)
//
//object Message {
//  implicit val encoder: Encoder[Message] = m => Json.obj("message" -> m.message.asJson)
//}

