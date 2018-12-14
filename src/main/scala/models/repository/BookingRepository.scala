package models.repository

import scala.concurrent.{ExecutionContext, Future}
import models._
import org.mongodb.scala._
import org.bson.types.ObjectId
import io.circe.syntax._

import java.util.Date
import java.text.SimpleDateFormat

trait BookingRepository {
  def all(): Future[Seq[Booking]]
  def findById(id: String): Future[Booking]
  def save(createBooking: CreateBooking): Future[String]
  def findByUserId(userId: String): Future[Seq[Booking]]
}

object BookingRepository {
  final case class BookingNotFound(id: String) extends Exception(s"Booking with id $id not found")
}

class BookingRepositoryMongo(collection: MongoCollection[Booking])(implicit ec: ExecutionContext) extends BookingRepository {
  override def all(): Future[Seq[Booking]] = {
    collection
      .find()
      .toFuture()
  }

  override def findById(id: String): Future[Booking] = {
    collection
      .find(Document("_id" -> new ObjectId(id)))
      .first()
      .toFuture()
  }

  override def save(createBooking: CreateBooking): Future[String] = {
    val booking = Booking(
      ObjectId.get(),
      new ObjectId(createBooking.carId),
      new ObjectId(createBooking.userId),
      new SimpleDateFormat("dd/MM/yyyy").parse(createBooking.from),
      new SimpleDateFormat("dd/MM/yyyy").parse(createBooking.to),
      createBooking.pickUp
    )
    collection
      .insertOne(booking)
      .head()
      .map { _ => booking._id.toHexString }
  }

  override def findByUserId(userId: String): Future[Seq[Booking]] = {
    collection
      .find(Document("userId" -> new ObjectId(userId)))
      .toFuture()
  }
}