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

  import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
  import io.circe.generic.auto._

  case class bookingRequest(token: String, carId: String, bookingDate: String, pickup: String, pickupDate: String, deliverPlace: String, deliverDate: String)
  val auth: Auth = Auth.getInstance()

  trait jsonBooking extends DefaultJsonProtocol {
    implicit val testAPIJsonFormat = jsonFormat7(bookingRequest)
  }

  override def route: Route = pathPrefix("booking") {
      post {
        entity(as[bookingRequest]) { params =>
          val token: String = params.token
          val bookingDate: String = params.bookingDate
          val from: String = params.pickupDate
          val to: String = params.deliverDate
          val pickup: String = params.pickup
          val deliverPlace: String = params.deliverPlace
          onComplete(tokenUid(token)) {
            case Success(uid: String) =>
              onComplete(carRepository.findById(params.carId)) {
                case Success(car: Car) =>
                  val rental = car.rental
                  val createBooking: CreateBooking =
                    new CreateBooking(userId = uid,car = car,bookingDate = bookingDate,pickUp = pickup,
                      pickupDate = from,deliverPlace = deliverPlace,deliverDate = to, rental = rental)
                  onComplete(bookingRepository.save(createBooking)){
                    case Success(d) =>
                      complete(StatusCodes.Created)
                    case Failure(e) =>
                      println(e)
                      complete(StatusCodes.InternalServerError)
                  }
                case Failure(exception) =>
                  println(exception)
                  complete(StatusCodes.ServerError, exception.getMessage)
              }
            case Failure(exception) =>
              complete(StatusCodes.InternalServerError, exception.getMessage())
          }
        }
      } ~
    pathPrefix("myBookings") {
      get {
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
    } ~
    pathPrefix("delete") {
      delete {
        parameter('bookingId) { bookingId =>
          onComplete(bookingRepository.deleteById(bookingId)) {
            case Success(deletedBooking ) =>
              complete(StatusCodes.OK, deletedBooking)
            case Failure(exception) =>
              complete(StatusCodes.NotFound, exception.getMessage())
          }
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
