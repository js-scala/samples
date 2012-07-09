package models

import scala.virtualization.lms.common.Base
import forest.lms._
import js._

case class ChatRoom(messages: List[Message])

trait ChatRoomOps extends Base with Fields {
  class ChatRoomOps()(implicit chatRoom: Rep[ChatRoom]) extends Fields[ChatRoom] {
    val messages = field[List[Message]]("messages")
  }
  implicit def toChatRoomOps(cr: Rep[ChatRoom]): ChatRoomOps = new ChatRoomOps()(cr)
}

