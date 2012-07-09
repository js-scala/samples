package models

import scala.virtualization.lms.common.Base
import forest.lms._
import js._

case class Message(author: String, content: String)

object Message {

  import play.api.libs.json._
  implicit object toJson extends Writes[Message] {
    def writes(message: Message): JsValue =
      JsObject(Seq(
        "author" -> JsString(message.author),
        "content" -> JsString(message.content)
      ))
  }
}

trait MessageOps extends Base with Fields {
  class MessageOps()(implicit person: Rep[Message]) extends Fields[Message] {
    val author = field[String]("author")
    val content = field[String]("content")
  }
  implicit def toMessageOps(m: Rep[Message]): MessageOps = new MessageOps()(m)
}

