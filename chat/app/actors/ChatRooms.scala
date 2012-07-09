package actors

import play.api._

import akka.actor._
import akka.actor.Actor._

import play.api.libs.iteratee._
import play.api.libs.concurrent._

class ChatRooms extends Actor {
  import PersonActor._

  var members = List.empty[PushEnumerator[String]]
  var children = List.empty[String]

  override def receive = {
    case Join() => {
      val stream = Enumerator.imperative[String]()
      members = stream :: members
      sender ! stream
    }
    case Quit() => {

    }
    case NewChild(name) => {
      children = name :: children
      println(members)
      for (member <- members) {
        member.push(name)
      }
    }
  }
}

object PersonActor {

  sealed trait Event
  case class NewChild(name: String) extends Event
  case class Join() extends Event
  case class Quit() extends Event

  lazy val system = ActorSystem("Person")
  lazy val ref = system.actorOf(Props[ChatRooms])

}