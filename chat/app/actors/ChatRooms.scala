package actors

import play.api._

import akka.actor._
import akka.actor.Actor._

import play.api.libs.iteratee._
import play.api.libs.concurrent._

import models._

class ChatRooms extends Actor {
  import ChatRooms.Events._

  var members = List.empty[(String, PushEnumerator[Message])] // (name, channel)
  var chatRoom = ChatRoom(Nil)
  var allMessages = views.Chat.chatRoom(chatRoom)

  override def receive = {
    case Join(username) => {
      lazy val stream: PushEnumerator[Message] = Enumerator.imperative(onComplete = { self ! Quit(stream) })
      members = (username -> stream) :: members
      self ! Posted(Message("system", "“%s” joined the chat room".format(username)))
      sender ! stream
    }
    case Quit(channel) => {
      for (member <- members find (_._2 == channel)) {
        members = members filterNot (_ == member)
        self ! Posted(Message("system", "“%s” left the chat room".format(member._1)))
      }
    }
    case Posted(message) => {
      chatRoom = ChatRoom(chatRoom.messages :+ message) // TODO Don’t use a List for appending
      allMessages = views.Chat.chatRoom(chatRoom)
      for ((_, channel) <- members) {
        channel.push(message)
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
    case class Join(username: String) extends Event
    case class Quit(channel: PushEnumerator[Message]) extends Event
    case object GetAllMessages extends Event
  }

  lazy val system = ActorSystem("ChatRooms")
  lazy val ref = system.actorOf(Props[ChatRooms])

}