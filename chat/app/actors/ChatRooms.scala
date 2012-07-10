package actors

import play.api._

import akka.actor._
import akka.actor.Actor._

import play.api.libs.iteratee._
import play.api.libs.concurrent._

import models._

class ChatRooms extends Actor {
  import ChatRooms.Events._

  var channels = List.empty[PushEnumerator[Message]] // readers connections
  var members = List.empty[String] // users chatting
  var chatRoom = ChatRoom(Nil)
  var allMessages = views.Chat.chatRoom(chatRoom)

  override def receive = {
    case GetAllMessages => {
      sender ! allMessages
    }
    case OpenChannel => {
      lazy val stream: PushEnumerator[Message] = Enumerator.imperative(onComplete = { self ! CloseChannel(stream) })
      channels = stream :: channels
      sender ! stream
    }
    case CloseChannel(channel) => {
      channels = channels filterNot (_ == channel)
    }
    case Join(username) => {
      members = username :: members
      self ! Post(Message("system", "“%s” joined the chat room".format(username)))
    }
    case Quit(username) => {
      if (members contains username) {
        members = members filterNot (_ == username)
        self ! Post(Message("system", "“%s” left the chat room".format(username)))
      }
    }
    case Post(message) => {
      chatRoom = chatRoom.copy(chatRoom.messages :+ message) // TODO Don’t use a List for appending
      allMessages = views.Chat.chatRoom(chatRoom)
      for (channel <- channels) {
        channel.push(message)
      }
    }
  }
}

object ChatRooms {

  sealed trait Event
  object Events {
    case class Post(message: Message) extends Event
    case class Join(username: String) extends Event
    case class Quit(username: String) extends Event
    case object GetAllMessages extends Event
    case object OpenChannel extends Event
    case class CloseChannel(c: PushEnumerator[Message]) extends Event
  }

  lazy val system = ActorSystem("ChatRooms")
  lazy val ref = system.actorOf(Props[ChatRooms])

}