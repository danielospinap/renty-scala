package controllers

import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.Location
import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport._
import models._
import models.repository._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}


class CarController(repository: CarRepository)(implicit ec: ExecutionContext, mat: Materializer) {
  val carRoutes =
    pathPrefix("cars") {
      (get & path(Segment).as(FindByIdRequest)) { request =>
        onComplete(repository.findById(request.id)) {
          case Success(Some(car)) =>
            complete(Marshal(car).to[ResponseEntity].map { e => HttpResponse(entity = e) })
          case Success(None) =>
            complete(HttpResponse(status = StatusCodes.NotFound))
          case Failure(e) =>
            complete(Marshal(Message(e.getMessage)).to[ResponseEntity].map { e => HttpResponse(entity = e, status = StatusCodes.InternalServerError) })
        }
      } ~ (post & pathEndOrSingleSlash & entity(as[Car])) { car =>
        onComplete(repository.save(car)) {
          case Success(id) =>
            complete(HttpResponse(status = StatusCodes.Created, headers = List(Location(s"cars/$id"))))
          case Failure(e) =>
            complete(Marshal(Message(e.getMessage)).to[ResponseEntity].map { e => HttpResponse(entity = e, status = StatusCodes.InternalServerError) })
        }
      }
    }
<<<<<<< HEAD
=======

  pathPrefix("cars/search") {
      (get & path(Segment).as(FindByIdRequest)) { request =>
        onComplete(repository.search()) {
          case Success(Some(car)) =>
            complete(Marshal(car).to[ResponseEntity].map { e => HttpResponse(entity = e) })
          case Success(None) =>
            complete(HttpResponse(status = StatusCodes.NotFound))
          case Failure(e) =>
            complete(Marshal(Message(e.getMessage)).to[ResponseEntity].map { e => HttpResponse(entity = e, status = StatusCodes.InternalServerError) })
        }
      } ~ (post & pathEndOrSingleSlash & entity(as[Car])) { car =>
        onComplete(repository.save(car)) {
          case Success(id) =>
            complete(HttpResponse(status = StatusCodes.Created, headers = List(Location(s"cars/search/"))))
          case Failure(e) =>
            complete(Marshal(Message(e.getMessage)).to[ResponseEntity].map { e => HttpResponse(entity = e, status = StatusCodes.InternalServerError) })
        }
      }
    }








>>>>>>> db944ad9fc75bee87f7ca897f568dbcc9f381a86
}
