package models.repository

import java.net.InetSocketAddress

import de.bwaldvogel.mongo.MongoServer
import de.bwaldvogel.mongo.backend.memory.MemoryBackend
import models.{Booking, Car, CreateCar, Rental}
import org.bson.codecs.configuration.CodecRegistries._
import org.bson.codecs.configuration.CodecRegistry
import org.bson.types.ObjectId
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.{Completed, MongoClient, MongoCollection, ServerAddress}
import org.scalatest.FunSuite

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Success

class CarRepositoryTest extends FunSuite {

  val server: MongoServer = new MongoServer(new MemoryBackend)
  val socketAddres: InetSocketAddress = server.bind()
  val severAddress: ServerAddress = new ServerAddress(socketAddres)
  val client: MongoClient = MongoClient("mongodb://" + severAddress.toString)
  val codecRegistry: CodecRegistry = fromRegistries(fromProviders(classOf[Car], classOf[CreateCar], classOf[Rental], classOf[Booking]), DEFAULT_CODEC_REGISTRY)
  val carCollection: MongoCollection[Car] = client.getDatabase("testDB").withCodecRegistry(codecRegistry).getCollection[Car]("cars")
  val carRepository: CarRepositoryMongo = new CarRepositoryMongo(carCollection)


  val pictures: List[String] = List("a", "b", "c")

  val rental = new Rental(
    123,
    "rental"
  )

  val car1: Car = new Car(
    ObjectId.get(),
    "brand",
    "thumbnail",
    "999",
    "type",
    "model",
    "plate",
    5,
    4,
    "transmission",
    4,
    "red",
    100,
    pictures,
    rental
  )

  val car2: Car = new Car(
    ObjectId.get(),
    "brand",
    "thumbnail",
    "999",
    "type",
    "model",
    "plate",
    5,
    4,
    "transmission",
    4,
    "red",
    100,
    pictures,
    rental
  )

  val car3: Car = new Car(
    ObjectId.get(),
    "brand",
    "thumbnail",
    "999",
    "type",
    "model",
    "plate",
    5,
    4,
    "transmission",
    4,
    "red",
    100,
    pictures,
    rental
  )

  test("Find all cars") {

    val insertFuture: Future[Completed] = carCollection.insertMany(Seq(car1, car2, car3)).toFuture()

    insertFuture onComplete {
      case Success(_) =>
        val carsFuture: Future[Seq[Car]] = carRepository.all()

        carsFuture onComplete {
          case Success(cars) =>
            val expectedCars: Seq[Car] = Seq(car1, car2, car3)
            assert(cars.equals(expectedCars))
        }
    }
  }

  test("find car by id") {
    carRepository.findById(car1._id.toHexString) onComplete {
      case Success(car) =>
        assert(car.equals(car1))
    }
  }

  test("get multiple cars by ids") {
    val ids = Seq(car1._id.toHexString, car2._id.toHexString, car3._id.toHexString)
    carRepository.getCarsById(ids) onComplete {
      case Success(cars) =>
        assert(cars.equals(Seq(car1, car2, car3)))
    }
  }

  test("save car from create car") {
    val createdCar = new CreateCar(
      "test brand",
      "test thumbnail",
      "test price",
      "test type",
      "test model",
      "test plate",
      1,
      1,
      "test transmission",
      1,
      "test color",
      1,
      pictures
    )

    carRepository.save(createdCar) onComplete {
      case Success(carid) =>
        carRepository.findById(carid) onComplete {
          case Success(car) =>
            assert(true)
        }
    }
  }
}
