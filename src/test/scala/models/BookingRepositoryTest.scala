package models
import org.scalatest.{BeforeAndAfter, FunSuite}
import com.github.simplyscala.{MongoEmbedDatabase, MongodProps}

class BookingRepositoryTest extends FunSuite with MongoEmbedDatabase with BeforeAndAfter{

  var mongoInstance: MongodProps = null

  before{  mongoInstance = mongoStart() }
  after { mongoStop(mongoInstance) }

  test("BookingRepository.findById") {

  }

}
