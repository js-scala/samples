package aspects

import scala.virtualization.lms.common.{Base, Structs}

trait Models { this: Base with Structs =>

  type Message = Record { val author: String; val content: String }
  def Message(a: Rep[String], c: Rep[String]): Rep[Message] =
    new Record { val author = a; val content = c }

  type ChatRoom = Record { val messages: List[Message] }
  def ChatRoom(ms: Rep[List[Message]]): Rep[ChatRoom] =
    new Record { val messages = ms }

}