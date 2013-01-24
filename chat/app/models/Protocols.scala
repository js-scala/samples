package models

object Protocols {

  import play.api.libs.json._
  implicit object toJson extends Writes[Message] {
    def writes(message: Message): JsValue =
      JsObject(Seq(
        "author" -> JsString(message.author),
        "content" -> JsString(message.content)
      ))
  }
}
