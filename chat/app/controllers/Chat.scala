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

trait Chat extends Controller with AuthenticationSupport { this: AuthenticationSettings =>
  import ChatRooms.Events._
  implicit val timeout = Timeout(5.seconds)
  
  def index = Authenticated { username => implicit request =>
    AsyncResult {
      ((ChatRooms.ref ? GetAllMessages).mapTo[String]).asPromise.map { allMessages =>
        Ok(views.html.index(allMessages))
      }
    }
  }

  def join = Authenticated { username => implicit request =>
    AsyncResult {
      ((ChatRooms.ref ? Join(username)).mapTo[Enumerator[Message]]).asPromise.map { message =>
        Ok.feed(message &> ToJson[Message] ><> EventSource[JsValue]()).as(EVENT_STREAM)
      }
    }
  }

  def postMessage = Authenticated { username => implicit request =>
    Form("content" -> nonEmptyText).bindFromRequest.fold(
      error => BadRequest,
      content => {
        Logger.info(username + " says " + content)
        ChatRooms.ref ! Posted(Message(username, content))
        Ok
      }
    )
  }
  
}