package controllers

import play.api.mvc._
import play.api.cache.Cache
import play.api.libs.json.Json
import play.api.Play.current
import models._
import models.repr._

class V2 extends Workspace with Controller with RenderSupport {

  def index = Action { implicit request =>
    render(MindMaps.ref().all)
  }

  def show(id: String, vs: ViewSettings) = Action { implicit request =>
    implicit val settings = vs
    MindMaps.ref().find(id) match {
      case Some(map) => render(MindMapR(id, map))
      case None => NotFound
    }
  }

  implicit val renderMindMaps: Render[Iterable[MindMapR]] = Render[Iterable[MindMapR]](
    "text/html" -> { ms: Iterable[MindMapR] => views.html.v2.index(ms) },
    "application/json" -> { ms: Iterable[MindMapR] => Cache.getOrElse("maps_json", 60 * 60)(Json.toJson(ms)) }
  )

  implicit def renderMindMap(implicit r: RequestHeader, vs: ViewSettings): Render[MindMapR] = Render[MindMapR](
    "text/html" -> { map: MindMapR => views.html.v2.show(map.id, vs) },
    "application/json" -> { map: MindMapR => Cache.getOrElse("map_"+map.id+"_json", 60 * 60)(Json.toJson(map.content)) }
  )

}