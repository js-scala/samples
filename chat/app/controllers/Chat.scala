package controllers

import play.api._
import play.api.mvc._
import play.api.libs.EventSource
import play.api.libs.json.JsValue
import play.api.libs.iteratee._
import play.api.libs.concurrent._
import play.api.data._
import play.api.data.Forms._

import akka.util.Timeout
import akka.util.duration._
import akka.pattern.ask

import actors.ChatRooms
import models._

object Chat extends Controller {
  import ChatRooms.Events._
  implicit val timeout = Timeout(5.seconds)
  
  def index = Action { implicit request =>
    AsyncResult {
      ((ChatRooms.ref ? GetAllMessages).mapTo[String]).asPromise.map { allMessages =>
        Ok(views.html.index(allMessages))
      }
    }
  }

  def join = Action {
    AsyncResult {
      ((ChatRooms.ref ? Join()).mapTo[Enumerator[Message]]).asPromise.map { message =>
        Ok.feed(message &> ToJson[Message] ><> EventSource[JsValue]()).as(EVENT_STREAM)
      }
    }
  }

  // FIXME Useless?
  def quit = Action {
    Ok("You have been disconected")
  }

  def postMessage = Action { implicit request =>
    Form(tuple(
      "author" -> nonEmptyText,
      "content" -> nonEmptyText
    )).bindFromRequest.fold(
      error => BadRequest,
      { case (author, content) =>
        Logger.info(author + " says " + content)
        ChatRooms.ref ! Posted(Message(author, content))
        Ok
      }
    )
  }
  
}