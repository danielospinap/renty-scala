package models.repository

import scala.concurrent.{ExecutionContext, Future}
import models._
import org.mongodb.scala._
import org.bson.types.ObjectId
import io.circe.syntax._
import java.util.Date
import java.text.SimpleDateFormat

import org.mongodb.scala.result.DeleteResult

trait BookingRepository {
  def all(): Future[Seq[Booking]]
  def findById(id: String): Future[Booking]
  def save(createBooking: CreateBooking): Future[String]
  def findByUserId(userId: String): Future[Seq[Booking]]
  def deleteById(id: String): Future[Booking]
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
      createBooking.userId,
      createBooking.car,
      new SimpleDateFormat("dd/MM/yyyy").parse(createBooking.bookingDate),
      createBooking.pickUp,
      new SimpleDateFormat("dd/MM/yyyy").parse(createBooking.pickupDate),
      createBooking.deliverPlace,
      new SimpleDateFormat("dd/MM/yyyy").parse(createBooking.deliverDate),
      createBooking.rental
    )
    collection
      .insertOne(booking)
      .head()
      .map { _ => booking._id.toHexString }
  }

  override def findByUserId(userId: String): Future[Seq[Booking]] = {
    collection
      .find(Document("userId" -> userId))
      .toFuture()
  }

  override def deleteById(id: String): Future[Booking] = {
    collection
      .findOneAndDelete(Document("_id" -> new ObjectId(id)))
      .toFuture()
  }
}
