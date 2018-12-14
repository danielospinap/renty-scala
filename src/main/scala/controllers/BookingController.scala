package controllers
import java.text.SimpleDateFormat

import akka.http.scaladsl.model.{HttpEntity, StatusCodes}
import akka.http.scaladsl.server.{Directives, Route}
import models.repository._
import spray.json.DefaultJsonProtocol
import com.firebase4s.auth.{Auth, FirebaseToken}
import models.{Car, CreateBooking}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class BookingController(bookingRepository: BookingRepository, carRepository: CarRepository)(implicit ec: ExecutionContext) extends Router with Directives {

  case class bookingRequest(token: String, carId: String, bookingDate: String, pickup: String, pickupDate: String, deliverPlace: String, deliverDate: String)
  val auth: Auth = Auth.getInstance()

  trait jsonBooking extends DefaultJsonProtocol {
    implicit val testAPIJsonFormat = jsonFormat7(bookingRequest)
  }

  override def route: Route = pathPrefix("booking") {
    pathEndOrSingleSlash {
      post {
        entity(as[bookingRequest]) { params =>
          val token: String = params.token
          val bookingDate = params.bookingDate
          val from = params.pickupDate
          val to = params.deliverDate
          val pickup = params.pickup
          val deliverPlace = params.deliverPlace
          onComplete(tokenUid(token)) {
            case Success(uid: String) =>
              onComplete(carRepository.findById(params.carId)) {
                case Success(car: Car) =>
                  val rental = car.rental
                  val createBooking: CreateBooking =
                    new CreateBooking(userId = uid,car = car,bookingDate = bookingDate,pickUp = pickup,
                      pickupDate = from,deliverPlace = deliverPlace,deliverDate = to, rental = rental)
                  bookingRepository.save(createBooking)
                  complete(StatusCodes.OK)
              }
            case Failure(exception) =>
              complete(StatusCodes.InternalServerError, exception.getMessage())
          }
        }
      } ~
      delete {

      }
    } ~
    path("" / ) {
      parameter('token) { token =>
        onComplete(tokenUid(token)) {
          case Success(uid) =>
            onComplete(bookingRepository.findByUserId(uid)) {
              case Success(bookings) =>
                complete(StatusCodes.OK, bookings)
            }
          case Failure(exception) =>
            complete(StatusCodes.NotFound, exception.getMessage())
        }
      }
    }
  }

  def tokenUid(token: String): Future[String] = {
    val uid: Future[String] = auth.verifyIdToken(token)
      .map((token: FirebaseToken) => token.uid)
    uid
  }
}
