package controllers

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

            val myList = cars.map(car => Car.encoder(car)).toList
            val y: JSONArray = new JSONArray(myList)
            println(y)

            complete(HttpResponse(status = StatusCodes.OK, entity = HttpEntity(ContentTypes.`application/json`, y.toString())))
        }

      } ~
      post {
        entity(as[CreateCar]) { createCar =>
          complete(carRepository.save(createCar))
        }
      }
   }
  }
}
