package models

import io.circe.syntax._
import io.circe._

import org.bson.types.ObjectId



case class User(_id: ObjectId,
               name: String,
               password: String,
               toquen: String,
               activeSession: Boolean
             
              ) {
  require(name != null, "name not informed")
  require(password != null, "password not informed")
  require(toquen != null, "toquen not informed")


  require(name.nonEmpty, "name cannot be empty")
  require(password.nonEmpty, "password cannot be empty")
  require(toquen.nonEmpty, "toquen cannot be empty")
  

}

object User {
  implicit val encoder: Encoder[User] = (myUser: User) => {
    Json.obj(

      "id" -> myUser._id.toHexString.asJson,
      "name" -> myUser.name.asJson,
      "password" -> myUser.password.asJson,
      "toquen" -> myUser.toquen.asJson,
      "activeSession" -> myUser.activeSession.asJson
   
    )
  }

  implicit val decoder: Decoder[User] = (c: HCursor) => {
    for {
      name <- c.downField("name").as[String]
      password <- c.downField("password").as[String]
      toquen <- c.downField("toquen").as[String]
      activeSession <- c.downField("activeSession").as[Boolean]
     
    } yield User(ObjectId.get(),
                name,
                password,
                toquen,
                activeSession
               
    )
  }
}

case class Messagee(message: String)

object Messagee {
  implicit val encoder: Encoder[Messagee] = m => Json.obj("message" -> m.message.asJson)
}

