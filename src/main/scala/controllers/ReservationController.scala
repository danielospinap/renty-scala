package controllers

import akka.http.scaladsl.server.{Directives, Route}
import models.repository.ReservationRepository

trait ReservationController {
  def route: Route
}

class RouterController(reservationRepository: ReservationRepository) extends ReservationController with Directives{

  import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
  import io.circe.generic.auto._

  override def route: Route = pathPrefix("reservations") {
    pathEndOrSingleSlash {
      get {
        complete(reservationRepository.getReservations())
      }
    }
  }
}
