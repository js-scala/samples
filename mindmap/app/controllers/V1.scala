package controllers

import play.api.mvc._
import models.MindMaps

class V1 extends Workspace with Controller {

  def index = Action {
    Ok(views.html.v1.index(MindMaps.ref().all))
  }

  def show(id: String, vs: ViewSettings) = Action { implicit request =>
    MindMaps.ref().find(id) match {
      case Some(map) => Ok(views.html.v1.show(id, map, vs))
      case None => NotFound
    }
  }

}