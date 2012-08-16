package models

import play.api.libs.json._
import play.api.mvc._
import play.api.cache.Cache
import play.api.Play.current
import controllers.{Render, ViewSettings}

object repr {

  implicit val mindMapRWrites: Writes[Iterable[MindMapR]] = new Writes[Iterable[MindMapR]] {
    def writes(ms: Iterable[MindMapR]): JsValue = JsArray(for (m <- ms.toSeq) yield {
      JsObject(Seq(
        "id" -> JsString(m.id),
        "content" -> JsObject(Seq(
          "name" -> JsString(m.content.name)
        ))
      ))
    })
  }

  // TODO Generate this
  implicit val mindMapWrites: Writes[MindMap] = new Writes[MindMap] {
    def writes(map: MindMap): JsValue = JsObject(Seq(
      //"id" -> JsNumber(map.id),
      "name" -> JsString(map.name),
      "vertices" -> JsArray(for (vertex <- map.vertices) yield {
        JsObject(Seq(
          "content" -> JsString(vertex.content),
          "posx" -> JsNumber(vertex.posx),
          "posy" -> JsNumber(vertex.posy),
          "width" -> JsNumber(vertex.width),
          "height" -> JsNumber(vertex.height)
        ))
      }),
      "edges" -> JsArray(for (edge <- map.edges) yield {
        JsObject(Seq(
          "origIdx" -> JsNumber(map.vertices.indexOf(edge.orig)),
          "endIdx" -> JsNumber(map.vertices.indexOf(edge.end))
        ))
      })
    ))
  }
}