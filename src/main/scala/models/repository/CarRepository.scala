package models.repository

import akka.japi.Option.Some
import org.mongodb.scala._
import org.mongodb.scala.bson.ObjectId
import models._
import models.repository.CarRepository.CarNotFound
import org.bson.types.ObjectId
import org.mongodb.scala.bson.conversions.Bson

import scala.concurrent.{ExecutionContext, Future}

trait CarRepository {
  def all(): Future[Seq[Car]]
  def findById(id: String): Future[Car]
  def save(createCar: CreateCar): Future[String]
  def getCarsById(listId: Seq[String]): Future[Seq[Car]]
}

object CarRepository {
  final case class CarNotFound(id: String) extends Exception(s"Car with id $id not found.")
}


class CarRepositoryMongo(collection: MongoCollection[Car])(implicit ec: ExecutionContext) extends CarRepository {

  import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
  import io.circe.generic.auto._
  def all(): Future[Seq[Car]] = {
    collection
      .find()
      .toFuture()


  }

  def findById(id: String): Future[Car] = {
    collection
      .find(Document("_id" -> new ObjectId(id)))
      .first
      .toFuture()
  }

  def save(createCar: CreateCar): Future[String] = {
    val list = List[String]()
    val rental = new Rental(ObjectId.get(), "samurai")
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
      createCar.pictures,
      rental
    )
    collection
      .insertOne(car)
      .head
      .map { _ => car._id.toHexString }
  }

  def getCarsById(listId: Seq[String]): Future[Seq[Car]] = {
    val filter = """{carId:{$in:""" + s"""[$listId]}}"""
    collection.find(Document(filter)).toFuture()
  }
}
