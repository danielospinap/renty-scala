package models

import io.circe
import io.circe.Json
import org.bson.types.ObjectId
import org.scalatest.FunSuite
import io.circe.syntax._
import io.circe._

class CarTest extends FunSuite{

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

  val car3: Car = new Car(
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

  test("Encode car") {

    val expectedEncodedCar: Json = Json.obj(
      "id"  -> car1._id.toHexString.asJson,
      "brand"  -> car1.brand.asJson,
      "thumbnail"  -> car1.thumbnail.asJson,
      "price"  -> car1.price.asJson,
      "type"  -> car1.carType.asJson,
      "model"  -> car1.model.asJson,
      "plate"  -> car1.plate.asJson,
      "rating"  -> car1.rating.asJson,
      "capacity"  -> car1.capacity.asJson,
      "transmission"  -> car1.transmission.asJson,
      "doors"  -> car1.doors.asJson,
      "color"  -> car1.color.asJson,
      "kms"  -> car1.kms.asJson,
      "pictures" -> car1.pictures.asJson,
      "rental"  -> car1.rental.asJson
    )

    val encodedCar: Json = Car.encoder(car1)

    assert(encodedCar.equals(expectedEncodedCar))

  }

  test("Encode cars") {
    val carList: Cars = new Cars(Seq(car1, car2, car3))

    val carListEncoded = Cars.encoder(carList)

    val expectedEncodedCars: Json = Json.obj(
      "list" -> Seq(car1, car2, car3).asJson
    )

    assert(carListEncoded.equals(expectedEncodedCars))
  }
}
