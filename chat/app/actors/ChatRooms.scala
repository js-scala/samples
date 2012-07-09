package actors

import play.api._

import akka.actor._
import akka.actor.Actor._

import play.api.libs.iteratee._
import play.api.libs.concurrent._

import models._

class ChatRooms extends Actor {
  import ChatRooms.Events._

  var members = List.empty[PushEnumerator[Message]]
  var chatRoom = ChatRoom(Nil)
  var allMessages = views.Chat.chatRoom(chatRoom)

  override def receive = {
    case Join() => {
      Logger.info("Someone joined the chat room")
      lazy val stream: PushEnumerator[Message] = Enumerator.imperative(onComplete = { self ! Quit(stream) })
      members = stream :: members
      sender ! stream
    }
    case Quit(channel) => {
      Logger.info("Someone left the chat room")
      members = members filterNot (_ == channel)
    }
    case Posted(message) => {
      chatRoom = ChatRoom(message :: chatRoom.messages)
      allMessages = views.Chat.chatRoom(chatRoom)
      for (member <- members) {
        member.push(message)
      }
    }
    case GetAllMessages => {
      sender ! allMessages
    }
  }
}

object ChatRooms {

  sealed trait Event
  object Events {
    case class Posted(message: Message) extends Event
    case class Join() extends Event
    case class Quit(channel: PushEnumerator[Message]) extends Event
    case object GetAllMessages extends Event
  }

  lazy val system = ActorSystem("ChatRooms")
  lazy val ref = system.actorOf(Props[ChatRooms])

}