package models.repository

import akka.japi.Option.Some
import org.mongodb.scala._
import org.mongodb.scala.bson.ObjectId
import models._
import models.repository.CarRepository.CarNotFound
import org.bson.types.ObjectId

import scala.concurrent.{ExecutionContext, Future}

trait CarRepository {
  def all(): Future[Seq[Car]]
  //def findById(id: String): Future[Car]
  def save(createCar: CreateCar): Future[String]
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

//  def findById(id: String): Future[Car] = {
//    collection
//      .find(Document("_id" -> new ObjectId(id)))
//      .first
//      .toFuture() {
//        case Some(foundCar) =>
//          Future.successful(foundCar)
//        case None =>
//          Future.failed(CarNotFound(id))
//      }
//  }

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
