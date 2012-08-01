package models

import akka.actor._
import akka.util.duration._

import play.api._
import play.api.libs.json._
import play.api.libs.iteratee._
import play.api.libs.concurrent._

import akka.util.Timeout
import akka.pattern.ask

import play.api.Play.current

object DrawingBoard {
  
  implicit val timeout = Timeout(1 second)
  
  lazy val default = {
    val drawingBoardActor = Akka.system.actorOf(Props[DrawingBoard])
    drawingBoardActor
  }

  def join(drawing: String):Promise[(Iteratee[JsValue,_],Enumerator[JsValue])] = {
    (default ? Join(drawing)).asPromise.map {      
      case Connected(id, enumerator) => 
        val iteratee = Iteratee.foreach[JsValue] { event =>
          default ! ClientAction(drawing, id, event)
        }.mapDone { _ =>
          default ! Quit(drawing, id)
        }
        (iteratee,enumerator)
    }
  }
}

class DrawingBoard extends Actor {
  var count = 0
  var members = Map.empty[Int,PushEnumerator[JsValue]]
  val colors =  IndexedSeq("red", "blue", "yellow", "green", "purple", "orange")
  
  def receive = {
    case Join(drawing) => {
      val channel =  Enumerator.imperative[JsValue]()
      val id = count
      count += 1
      members = members + (id -> channel)
      sender ! Connected(id, channel)
    }
    case ClientAction(drawing, id, js) => {
      notifyAll(drawing, id, js)
    }
    case Quit(drawing, id) => {
      members = members - id
    }
  }

  def pickColor(id: Int) = colors(id % colors.length)

  def notifyAll(drawing: String, id: Int, js: JsValue) {
    val msg = (js \ "action").as[String] match {
      case "move" => Some(JsObject(Seq(
        "action" -> JsString("move"),
        "id"     -> JsString(id.toString),
        "color"  -> JsString(pickColor(id)),
        "cx"     -> js \ "cx",
        "cy"     -> js \ "cy",
        "w"      -> js \ "w",
        "h"      -> js \ "h"
      )))
      case _ => None
    }
    msg match {
      case None => Logger("debug").info("Received unknown message: " + js.toString)
      case Some(msg) => {
        members.foreach { 
          case (_, channel) => channel.push(msg)
        }        
      }
    }
  }  
}

case class Join(drawing: String)
case class Quit(drawing: String, id: Int)
case class ClientAction(drawing: String, id: Int, js: JsValue)
case class Connected(id: Int, enumerator:Enumerator[JsValue])
