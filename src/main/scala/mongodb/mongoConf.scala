package mongodb

import com.typesafe.config.ConfigFactory
import org.mongodb.scala.bson.codecs._
import org.bson.codecs.configuration.CodecRegistries._
import org.mongodb.scala._
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._

import models._

object Mongo {
  lazy val config = ConfigFactory.load()
  lazy val mongoClient: MongoClient = MongoClient(System.getenv("DB_URL"))
  lazy val codecRegistry = fromRegistries(fromProviders(classOf[Car], classOf[CreateCar]), DEFAULT_CODEC_REGISTRY)
  lazy val database: MongoDatabase = mongoClient.getDatabase("renty").withCodecRegistry(codecRegistry)
  lazy val carCollection: MongoCollection[Car] = database.getCollection[Car]("cars")
}
