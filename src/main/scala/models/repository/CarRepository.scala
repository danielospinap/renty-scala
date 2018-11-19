package models.repository

import org.mongodb.scala._
import org.mongodb.scala.bson.ObjectId

import models._

import scala.concurrent.{ExecutionContext, Future}


class CarRepository(collection: MongoCollection[Car])(implicit ec: ExecutionContext) {
  def findById(id: String): Future[Option[Car]] =
    collection
    .find(Document("_id" -> new ObjectId(id)))
    .first
    .head()
    .map(Option(_))

  def save(car: Car): Future[String] =
    collection
    .insertOne(car)
    .head
    .map { _ => car._id.toHexString }
}
