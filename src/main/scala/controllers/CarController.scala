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


class CarController(carRepository: CarRepository)(implicit ec: ExecutionContext) extends Router with Directives {

  import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
  import io.circe.generic.auto._

  override def route: Route = pathPrefix("cars") {
    pathEndOrSingleSlash {
      get {
        onComplete(carRepository.all()) {
          case Success(cars: Seq[Car]) =>
            complete(StatusCodes.OK, cars)
          case Failure(exception) =>
            complete(StatusCodes.InternalServerError, exception.getMessage())
        }

      } ~
      post {
        entity(as[CreateCar]) { createCar =>
          complete(carRepository.save(createCar))
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
//    } ~ path(Segment) { id: String =>
//      get {
//        onComplete(carRepository.findById(id)) {
//          case Success(car) =>
//            complete(Marshal(car).to[ResponseEntity].map { e => HttpResponse(entity = e) })
//          case Failure(exception) =>
//            complete(Marshal(Message(e.getMessage)).to[ResponseEntity].map { e => HttpResponse(entity = e, status = StatusCodes.InternalServerError) })
//
//        }
//      }
//
//    }
  }

//  val carRoutes =
//    pathPrefix("cars") {
//      (get & path(Segment).as(FindByIdRequest)) { request =>
//        onComplete(repository.findById(request.id)) {
//          case Success(Some(car)) =>
//            complete(Marshal(car).to[ResponseEntity].map { e => HttpResponse(entity = e) })
//          case Success(None) =>
//            complete(HttpResponse(status = StatusCodes.NotFound))
//          case Failure(e) =>
//            complete(Marshal(Message(e.getMessage)).to[ResponseEntity].map { e => HttpResponse(entity = e, status = StatusCodes.InternalServerError) })
//        }
//      } ~ (post & pathEndOrSingleSlash & entity(as[Car])) { car =>
//        onComplete(repository.save(car)) {
//          case Success(id) =>
//            complete(HttpResponse(status = StatusCodes.Created, headers = List(Location(s"cars/$id"))))
//          case Failure(e) =>
//            complete(Marshal(Message(e.getMessage)).to[ResponseEntity].map { e => HttpResponse(entity = e, status = StatusCodes.InternalServerError) })
//        }
//      }
//    }
}
