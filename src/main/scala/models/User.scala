package models

import io.circe.syntax._
import io.circe._
import org.bson.types
import org.bson.types.ObjectId


import scala.beans.BeanProperty
import java.io.{File,IOException}
import java.io.FileInputStream
import java.io.InputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.stream.Collectors
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database._
import com.twitter.util.{Future,Promise}
import com.twitter.util.Future

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

import scala.concurrent.{Await, ExecutionContext}
import scala.util.{Failure, Success}
import controllers._

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



import scala.collection.JavaConverters._
import scala.collection.breakOut

case class FindByIdRequest(id: String) {
  require(ObjectId.isValid(id), "the informed id is not a representation of a valid hex string")
}


case class FirebaseException(s:String) extends Exception(s)

case class UserNotFoundException(s:String) extends Exception(s)
object Firebase {  
     
  val resourcesPath = getClass.getClassLoader.getResource("curso.json")
    println(resourcesPath.getPath.toString())    
 
   val serviceAccount:InputStream = getClass.getClassLoader.getResourceAsStream("curso.json")
 
  val options = new FirebaseOptions.Builder()
 .setDatabaseUrl("https://renty-vue.firebaseio.com")
 .setServiceAccount(serviceAccount)
 .build()

 FirebaseApp.initializeApp(options)
  val database = FirebaseDatabase.getInstance()
 def ref(path: String): DatabaseReference = database.getReference(path)
     println("listo el llopo")    


}

case class User(name: String,password: String,token: String)
                {
                  def toBean ={
                    val user = new UserBean()
                    user.name = name 
                    user.password = password 
                    user.token = token 
                    user
                  }
                  
                  
                }
object User{
def create(user: User): Future[User] = {
                  val ref = Firebase.ref(s"users/${user.name}")
                 
                  val userRecord = user.toBean
                  val p = new Promise[User]
                  ref.setValue(userRecord, new DatabaseReference.CompletionListener(){
                    def onComplete(databaseError: DatabaseError, databaseReference: DatabaseReference){
                      if(databaseError != null){
                        p.setException(FirebaseException(databaseError.getMessage))
                      }else{
                        p.setValue(user)
        }
      }
    })
    p
  }  

def findById(id: String): Future[User] = {
    val ref = Firebase.ref(s"renty-vue/$id")
    val p = new Promise[User]
    ref.addListenerForSingleValueEvent(new ValueEventListener() {
       def onDataChange(snapshot: DataSnapshot) =  {
        val userRecord: UserBean = snapshot.getValue(classOf[UserBean])
        if(userRecord != null){
          p.setValue(userRecord.toCase)
        }else{
          p.setException(UserNotFoundException(s"User  not found."))
        }
      }
       def onCancelled(databaseError: DatabaseError) = {
        p.setException(FirebaseException(databaseError.getMessage))
      }
    })
    p
  }

  def save(user: User): Future[User] = {
                 //lógica
    val userr = new Promise[User]
    userr
  }   

}

class UserBean() {
  @BeanProperty var name:String = null
  @BeanProperty var password:String = null
  @BeanProperty var token:String = null
  
  def toCase:User =  {
    User(name,password,token)
  }

  
}




 



