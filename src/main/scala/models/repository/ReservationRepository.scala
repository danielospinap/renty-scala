package models.repository

import com.mongodb.async.client.MongoCollection
import models.Reservation

import scala.concurrent.{ExecutionContext, Future}

class ReservationRepository(collection: MongoCollection[Reservation])(implicit ec: ExecutionContext) {

  def getReservations(tokenId: String): Future[Seq[Reservation]] =
  def deleteReservation(id: String): Future[Seq[Reservation]] =
    collection
    .deleteOne()
}

