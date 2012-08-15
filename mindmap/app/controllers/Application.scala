package controllers

import play.api._
import play.api.mvc._
import play.api.libs.concurrent._
import concurrent.stm.Ref

import models._
import models.repr._

object Application extends Controller with RenderSupport {

  val maps = Ref(MindMaps()).single

  def commit(e: Event) {
    maps.transform(_.apply(e))
  }

  MindMaps.init.foreach(commit)

  def index = Action {
    Ok(views.html.index(maps().all))
  }

  def create = TODO

  def show(id: String, vs: ViewSettings) = Action { implicit request =>
    implicit val settings = vs
    maps().find(id) match {
      case Some(map) => render(MindMapR(id, map))
      case None => NotFound
    }
  }

  def update(id: String) = TODO


  def seofriendly(id: String, vs: ViewSettings) = Action { implicit request =>
    maps().find(id) match {
      case Some(map) => Ok(views.html.show(id, map, vs, "seofriendly"))
      case None => NotFound
    }
  }

  def serverfriendly(id: String, vs: ViewSettings) = Action { implicit request =>
    Ok(views.html.show2(id, vs, "serverfriendly"))
  }

  def AsyncAction(r: => Promise[Result]) = Action(AsyncResult(r))
  def AsyncAction(f: RequestHeader => Promise[Result]) = Action(r => AsyncResult(f(r)))

}