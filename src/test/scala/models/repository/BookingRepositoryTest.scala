package models.repository

import java.net.InetSocketAddress

import de.bwaldvogel.mongo.MongoServer
import de.bwaldvogel.mongo.backend.memory.MemoryBackend
import models.{Booking, Car, CreateCar, Rental}
import org.bson.codecs.configuration.CodecRegistries._
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.bson.collection.mutable.Document
import org.mongodb.scala.{MongoClient, ServerAddress}
import org.scalatest.FunSuite

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global

class BookingRepositoryTest extends FunSuite {

  val server = new MongoServer(new MemoryBackend)
  val severAddress: InetSocketAddress = server.bind()
  val saddress = new ServerAddress(severAddress)
  val client = MongoClient("mongodb://" + saddress.toString)
  val codecRegistry = fromRegistries(fromProviders(classOf[Car], classOf[CreateCar], classOf[Rental], classOf[Booking]), DEFAULT_CODEC_REGISTRY)
  val bookingCollection = client.getDatabase("testDB").withCodecRegistry(codecRegistry).getCollection[Booking]("bookings")
  val bookingRepository = new BookingRepositoryMongo(bookingCollection)


  test("BookingRepository.findById") {

//    bookingCollection.insertOne(Document())

    val x = bookingRepository.all()

    x.onComplete(bookings => {
      assert(1===1)
      println(bookings)
    }
    )
  }

}
