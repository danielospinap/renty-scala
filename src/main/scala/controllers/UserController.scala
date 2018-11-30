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


class UserController(repository: UserRepository)(implicit ec: ExecutionContext, mat: Materializer) {
  val userRoutes =
    pathPrefix("users") {
      (get & path(Segment).as(FindUserByIdRequest)) { request =>
        onComplete(repository.findUserById(request.id)) {
          case Success(Some(user)) =>
            complete(Marshal(user).to[ResponseEntity].map { e => HttpResponse(entity = e) })
          case Success(None) =>
            complete(HttpResponse(status = StatusCodes.NotFound))
          case Failure(e) =>
            complete(Marshal(Message(e.getMessage)).to[ResponseEntity].map { e => HttpResponse(entity = e, status = StatusCodes.InternalServerError) })
        }
      } ~ (post & pathEndOrSingleSlash & entity(as[User])) { user =>
        onComplete(repository.save(user)) {
          case Success(id) =>
            complete(HttpResponse(status = StatusCodes.Created, headers = List(Location(s"users/$id"))))
          case Failure(e) =>
            complete(Marshal(Message(e.getMessage)).to[ResponseEntity].map { e => HttpResponse(entity = e, status = StatusCodes.InternalServerError) })
        }
      }
    }
}
