//Configuración DB firebase

import akka.NotUsed
import akka.stream.Materializer
import akka.stream.scaladsl.{Flow, Sink, Keep}
import com.elkozmon.akka.firebase.Document
import com.elkozmon.akka.firebase.scaladsl._
import com.google.firebase.database.FirebaseDatabase

object Test {

  implicit val mat: Materializer = ???

  val transform: Flow[Document, Document, NotUsed] = ???
  
  val parallelism: Int = 512

  val consumer = Consumer.asyncSource(
    sourceNode = FirebaseDatabase.getInstance().getReference("my-source"),
    bufferSize = 256
    )

  val producer = Producer.asyncFlow(
    targetNode = FirebaseDatabase.getInstance().getReference("my-sink")
  )

  val (consumerControl, futureDone) = consumer
    .mapAsync(parallelism)(identity)
    .via(transform)
    .via(producer)
    .mapAsync(parallelism)(identity)
    .toMat(Sink.ignore)(Keep.both)
    .run()
}