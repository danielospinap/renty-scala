package models.repository

import org.mongodb.scala._
import org.mongodb.scala.bson.ObjectId
import models._
import org.bson.types.ObjectId

import scala.concurrent.{ExecutionContext, Future}

object CarRepository {
  final case class CarNotFound(id: String) extends Exception(s"Car with id $id not found.")
}


class CarRepository(collection: MongoCollection[Car])(implicit ec: ExecutionContext) {

  def all(): Future[Seq[Car]] =
    collection
    .find()
    .toFuture()

  def findById(id: String): Future[Option[Car]] =
    collection
    .find(Document("_id" -> new ObjectId(id)))
    .first
    .head()
    .map(Option(_))

  def save(createCar: CreateCar): Future[String] ={
    val car = Car(
      ObjectId.get(),
      createCar.brand,
      createCar.thumbnail,
      createCar.price,
      createCar.carType,
      createCar.model,
      createCar.plate,
      createCar.rating,
      createCar.capacity,
      createCar.transmission,
      createCar.doors,
      createCar.color,
      createCar.kms,
      createCar.pictures
    )
    collection
      .insertOne(car)
      .head
      .map { _ => car._id.toHexString }
  }
}
