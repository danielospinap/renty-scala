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

 
}
