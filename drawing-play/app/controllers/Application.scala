package controllers

import play.api._
import play.api.mvc._

import play.api.libs.json._
import play.api.libs.iteratee._

import models._

import akka.actor._

object Application extends Controller {
  
  def index = Action { implicit request =>
    Ok(views.html.index())
  }

  def drawing(name: String) = Action { implicit request =>
    Ok(views.html.drawing(name))
  }

  def cursor(name: String) = Action { implicit request =>
    Ok(views.html.cursor(name)(views.html.empty()))
  }

  def socket(name: String, mode: String) = WebSocket.async[JsValue] { request  =>
    DrawingBoard.join(name, mode)
  }
}