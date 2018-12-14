package controllers

import akka.http.javadsl.model.ws.Message
import akka.http.scaladsl.marshalling.{Marshal, ToResponseMarshallable}
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.Location
import akka.http.scaladsl.server.{Directives, Route}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.Materializer
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport._
import io.circe.Json
import models._
import models.repository._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.parsing.json.JSONArray
import scala.util.{Failure, Success}
import java.text.SimpleDateFormat
import java.util.Date


class CarController(carRepository: CarRepository, bookingRepository: BookingRepository)(implicit ec: ExecutionContext) extends Router with Directives {

  import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
  import io.circe.generic.auto._

  override def route: Route = pathPrefix("cars") {
    pathPrefix("search") {
      get {
        parameters('from, 'to, 'type, 'pickup) { (from, to, `type`, pickup ) =>

          onComplete(carRepository.all()) {
            case Success(cars: Seq[Car]) =>

              val dateFrom = new SimpleDateFormat("dd/MM/yyyy").parse(from)
              val dateTo = new SimpleDateFormat("dd/MM/yyyy").parse(to)
              onComplete(bookingRepository.all()) {
                case Success(bookings: Seq[Booking]) =>
                  val filteredBookings: Seq[Booking] = bookings.filter(booking => dateFrom.after(booking.to))
                  val carIds = filteredBookings.map(booking => booking.carId.toHexString)
                  onComplete(carRepository.getCarsById(carIds)) {
                    case Success(newCars: Seq[Car]) =>
                      val filteredCars = newCars.filter(car => car.carType == `type`)
                      complete(StatusCodes.OK, filteredCars)
                  }
              }
            case Failure(exception) =>
              complete(StatusCodes.InternalServerError, exception.getMessage())
          }
        }
      } ~
      post {
        entity(as[CreateCar]) { createCar =>
          complete(carRepository.save(createCar))
        }
      }
//     ~ pathPrefix("search"){
//      get {
//        parameters('from.as[String], 'to.as[String], 'pickup.as[String]) { (from, to, pickup) => {
//          onComplete(carRepository.all()) {
//            case Success(cars: Seq[Car]) =>
//              val myList = cars.map(car => Car.encoder(car)).toList
//
//              complete(HttpResponse(status = StatusCodes.OK, entity = HttpEntity(ContentTypes.`application/json`, myList.toString())))
//          }
//        }
//        }
//      }
    } ~ path(Segment) { id: String =>
      get {
        onComplete(carRepository.findById(id)) {
          case Success(car) =>
            complete(StatusCodes.OK, car)
          case Failure(exception) =>
            complete(StatusCodes.InternalServerError, exception.getMessage())

        }
      }

    }
  }
}
//    } ~ pathPrefix("search"){
//      get {
//        parameters('from.as[String], 'to.as[String], 'pickup.as[String]) { (from, to, pickup) => {
//          onComplete(carRepository.all()) {
//            case Success(cars: Seq[Car]) =>
//              val myList = cars.map(car => Car.encoder(car)).toList
//
//              complete(HttpResponse(status = StatusCodes.OK, entity = HttpEntity(ContentTypes.`application/json`, myList.toString())))
//          }
//        }
//        }
//      }
