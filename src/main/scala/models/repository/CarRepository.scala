package models.repository

import akka.japi.Option.Some
import org.mongodb.scala._
import org.mongodb.scala.bson.ObjectId
import models._
import models.repository.CarRepository.CarNotFound
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

  def findById(id: String): Future[Car] = {
    collection
      .find(Document("_id" -> new ObjectId(id)))
      .first
      .toFuture() {
        case Some(foundCar) =>
          Future.successful(foundCar)
        case None =>
          Future.failed(CarNotFound(id))
      }
  }

  def save(createCar: CreateCar): Future[String] = {
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
