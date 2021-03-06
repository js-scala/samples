package controllers

import play.api.mvc._
import models._

class V3 extends Workspace with Controller {

  def index = Action {
    Ok(views.html.v3.index(MindMaps.ref().all))
  }

  def show(id: String, vs: ViewSettings) = Action { implicit request =>
    MindMaps.ref().find(id) match {
      case Some(map) => Ok(views.html.v3.show(id, map, vs))
      case None => NotFound
    }
  }

}