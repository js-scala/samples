package controllers

import play.api._
import play.api.mvc._
import play.api.libs.EventSource
import play.api.libs.json.JsValue
import play.api.libs.iteratee._
import play.api.libs.concurrent._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.data._
import play.api.data.Forms._

import akka.util.Timeout
import akka.pattern.ask

import actors.ChatRooms
import models._
import models.Protocols._
import java.util.concurrent.TimeUnit

object Chat extends Controller {
  import ChatRooms.Events._
  implicit val timeout = Timeout(5, TimeUnit.SECONDS)
  
  def index = Action { implicit request =>
    Async {
      ((ChatRooms.ref ? GetAllMessages).mapTo[scala.xml.NodeSeq]).map { allMessages =>
        Ok(views.html.index(allMessages, authenticatedUser))
      }
    }
  }

  def login(u: String) = Action { implicit request =>
    Form("username" -> nonEmptyText).bindFromRequest.fold(
      badForm => BadRequest,
      username => {
        ChatRooms.ref ? Join(username)
        Ok.withSession(session + (USERNAME -> username))
      }
    )
  }

  def logout = Action { implicit request =>
    for (username <- session.get(USERNAME)) {
      ChatRooms.ref ? Quit(username)
    }
    Ok.withSession(session - USERNAME)
  }

  def messages = Action { implicit request =>
    Async {
      ((ChatRooms.ref ? OpenChannel).mapTo[Enumerator[Message]]).map { message =>
        Ok.feed(message &> ToJson[Message] ><> EventSource()).as(EVENT_STREAM)
      }
    }
  }

  def postMessage = Authenticated { username => implicit request =>
    Form("content" -> nonEmptyText).bindFromRequest.fold(
      error => BadRequest,
      content => {
        ChatRooms.ref ! Post(Message(username, content))
        Ok
      }
    )
  }

  def authenticatedUser(implicit request: RequestHeader): Option[String] =
    session.get(USERNAME)

  def isAuthenticated(implicit request: RequestHeader) =
    authenticatedUser.isDefined

  def Authenticated(f: String => Request[_] => Result) = Action { implicit request =>
    authenticatedUser match {
      case Some(username) => f(username)(request)
      case None => Forbidden
    }
  }

  val USERNAME = "username"

}