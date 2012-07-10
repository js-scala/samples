package models

import play.api.libs.json._
import play.api.libs.iteratee._

object ToJson {
	def apply[A : Writes]: Enumeratee[A, JsValue] =
	  Enumeratee.map(a => Json.toJson(a))
}