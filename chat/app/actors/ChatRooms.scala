package actors

import play.api._

import akka.actor._
import akka.actor.Actor._

import play.api.libs.iteratee._
import play.api.libs.concurrent._

class ChatRooms extends Actor {
  import PersonActor._
  import models._

  var members = List.empty[PushEnumerator[String]]
  var person = Person(Nil)
  var allChildren = views.Chat.allChildren(person)

  override def receive = {
    case Join() => {
      Logger.info("Someone joined the chat room")
      lazy val stream: PushEnumerator[String] = Enumerator.imperative(onComplete = { self ! Quit(stream) })
      members = stream :: members
      sender ! stream
    }
    case Quit(channel) => {
      Logger.info("Someone left the chat room")
      members = members filterNot (_ == channel)
    }
    case NewChild(name) => {
      person = Person(name :: person.children)
      allChildren = views.Chat.allChildren(person)
      for (member <- members) {
        member.push(name)
      }
    }
    case AllChildren => {
      sender ! allChildren
    }
  }
}

object PersonActor {

  sealed trait Event
  case class NewChild(name: String) extends Event
  case class Join() extends Event
  case class Quit(channel: PushEnumerator[String]) extends Event
  case object AllChildren extends Event

  lazy val system = ActorSystem("Person")
  lazy val ref = system.actorOf(Props[ChatRooms])

}