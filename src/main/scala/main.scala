import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

import scala.concurrent.{Await, ExecutionContext}
import scala.util.{Failure, Success}
import controllers._
import models.repository.{CarRepository, CarRepositoryMongo}
import mongodb.Mongo
import scala.beans.BeanProperty
import com.twitter.util.{Future,Promise}

import java.io.InputStream
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database._
import scala.collection.JavaConverters._
import scala.collection.breakOut
import models._

import akka.http.scaladsl.marshalling.Marshal
////
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.Location
import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport._
import models.repository._

import scala.concurrent.ExecutionContext
////




object Main extends App {
 implicit val sys: ActorSystem = ActorSystem("akka-http-mongodb-microservice")
 implicit val mat: ActorMaterializer = ActorMaterializer()
 implicit val ec: ExecutionContext = sys.dispatcher

  val log = sys.log

 val routes =
      path("hello") {
        get {
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "${user.name}"))
        }
      } ~
  pathPrefix("user" / IntNumber) { userId =>
    pathEnd {
     
     complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, User.findById(userId.toString()).map(user=>println(s"Retrieved ${user.name}")).toString()))

      } 
    }~
  
    pathPrefix("create"/ IntNumber) { userId1  =>
    pathEnd {
        val gilbert = new User(userId1.toString(),"pass","1234")
        User.create(gilbert).map(user => println(s"Created ${user.name}"))
     complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "succesfull"))
      }
    }

 println("")

  Http().bindAndHandle(routes, "0.0.0.0", System.getenv("PORT").toInt).onComplete {
    case Success(b) => log.info(s"application is up and running at ${b.localAddress.getHostName}:${b.localAddress.getPort}")
    case Failure(e) => log.error(s"could not start application: {}", e.getMessage)
  }
}
