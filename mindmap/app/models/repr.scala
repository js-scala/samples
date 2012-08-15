package models

import play.api.libs.json._
import play.api.mvc._
import play.api.cache.Cache
import play.api.Play.current
import controllers.{Render, ViewSettings}

object repr {

  implicit def renderMindMap(implicit r: RequestHeader, vs: ViewSettings): Render[MindMapR] = Render[MindMapR](
    "text/html" -> { map: MindMapR => views.html.show(map.id, map.content, vs, "show") },
    "application/json" -> { map: MindMapR => Cache.getOrElse("map_"+map.id+"_json", 60 * 60)(Json.toJson[MindMap](map.content)) }
  )

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