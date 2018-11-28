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

<<<<<<< HEAD
=======
 def search(): Future[Option[Car]] =
    collection
    .find(Document())
    //.first
   .head()
   .map(Option(_))




>>>>>>> db944ad9fc75bee87f7ca897f568dbcc9f381a86
  def save(car: Car): Future[String] =
    collection
    .insertOne(car)
    .head
    .map { _ => car._id.toHexString }
<<<<<<< HEAD
=======




  
>>>>>>> db944ad9fc75bee87f7ca897f568dbcc9f381a86
}
