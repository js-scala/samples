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

  def join(name: String, mode: String):Promise[(Iteratee[JsValue,_],Enumerator[JsValue])] = {
    (default ? Join(name, mode)).asPromise.map {
      case Connected(id, enumerator) => 
        val iteratee = Iteratee.foreach[JsValue] { event =>
          default ! ClientAction(name, id, event)
        }.mapDone { _ =>
          default ! Quit(name, id)
        }
        (iteratee,enumerator)
    }
  }
}

class DrawingBoard extends Actor {
  var count = 0
  var members = Map.empty[Int,PushEnumerator[JsValue]]
  var messages = List.empty[JsValue]
  val colors =  IndexedSeq("red", "blue", "yellow", "green", "purple", "orange")

  def receive = {
    case Join(name, mode) => {
      val id = count
      count += 1
      val channel =  mode match {
        case "goto" =>
          Enumerator.imperative[JsValue]()
        case "replay" =>
          Enumerator.imperative[JsValue](onStart = self ! Playback(name, id))
      }
      members = members + (id -> channel)
      sender ! Connected(id, channel)
    }
    case Playback(name, id) => {
      members.get(id).foreach { channel =>
        messages.reverse.foreach(channel.push)
      }
    }
    case ClientAction(name, id, js) => {
      notifyAll(name, id, js)
    }
    case Quit(name, id) => {
      members = members - id
    }
  }

  def pickColor(id: Int) = colors(id % colors.length)

  def notifyAll(name: String, id: Int, js: JsValue) {
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
        messages = msg :: messages
        members.foreach { 
          case (_, channel) => channel.push(msg)
        }        
      }
    }
  }  
}

case class Join(name: String, mode: String)
case class Playback(name: String, id: Int)
case class Quit(name: String, id: Int)
case class ClientAction(name: String, id: Int, js: JsValue)
case class Connected(id: Int, enumerator:Enumerator[JsValue])
