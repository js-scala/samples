package controllers

import play.api._
import play.api.mvc._

import play.api.libs.json._
import play.api.libs.iteratee._

import models._

import akka.actor._
import akka.util.duration._

object Application extends Controller {
  
  def index = Action { implicit request =>
    Ok(views.html.index())
  }

  def drawing(drawingName: String) = WebSocket.async[JsValue] { request  =>
    DrawingBoard.join(drawingName)
  }

}