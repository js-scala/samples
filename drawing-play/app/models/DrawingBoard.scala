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

  val colors =  IndexedSeq("red", "blue", "yellow", "green", "purple", "orange")
  def pickColor(id: Int) = colors(id % colors.length)

  lazy val supervisor = {
    val drawingBoardSupervisor = Akka.system.actorOf(Props[DrawingBoardSupervisor])
    drawingBoardSupervisor
  }

  def join(name: String, mode: String):Promise[(Iteratee[JsValue,_],Enumerator[JsValue])] = {
    (supervisor ? Join(name, mode)).asPromise.map {
      case Connected(board, id, enumerator) =>
        val iteratee = Iteratee.foreach[JsValue] { event =>
          board ! ClientAction(id, event)
        }.mapDone { _ =>
          board ! Quit(id)
        }
        (iteratee,enumerator)
    }
  }
}

class DrawingBoard(val name: String) extends Actor {
  val filename = "drawings/" + name + ".json"

  private var count = 0
  private var members = Map.empty[Int,PushEnumerator[JsValue]]
  private var messages = List.empty[JsValue]

  def receive = {
    case JoinBoard(client, mode) => {
      val id = count
      count += 1
      val channel =  mode match {
        case "goto" =>
          Enumerator.imperative[JsValue]()
        case "replay" =>
          Enumerator.imperative[JsValue](onStart = self ! Playback(id))
      }
      members = members + (id -> channel)
      client ! Connected(self, id, channel)
    }
    case Playback(id) => {
      members.get(id).foreach { channel =>
        messages.reverse.foreach(channel.push)
      }
    }
    case ClientAction(id, js) => {
      notifyAll(id, js)
    }
    case Quit(id) => {
      members = members - id
      self ! SaveBoard
    }
    case LoadBoard => {
      val f = new java.io.File(filename)
      if (f.exists) {
        Logger.info("Loading " + name)
        val js = Json.parse(io.Source.fromFile(f).mkString)
        messages = messages ::: js.as[Array[JsValue]].toList
      }
    }
    case SaveBoard => {
      Logger.info("Saving " + name)
      val out = new java.io.PrintWriter(filename)
      try {
        out.println(JsArray(messages).toString())
      } finally { out.close() }
    }
  }

  def notifyAll(id: Int, js: JsValue) {
    val msg = (js \ "action").as[String] match {
      case "move" => Some(JsObject(Seq(
        "action" -> JsString("move"),
        "id"     -> JsString(id.toString),
        "color"  -> JsString(DrawingBoard.pickColor(id)),
        "cx"     -> js \ "cx",
        "cy"     -> js \ "cy",
        "w"      -> js \ "w",
        "h"      -> js \ "h"
      )))
      case _ => None
    }
    msg match {
      case None => Logger.info("Received unknown message: " + js.toString)
      case Some(msg) => {
        messages = msg :: messages
        members.foreach {
          case (_, channel) => channel.push(msg)
        }
      }
    }
  }
}

class DrawingBoardSupervisor extends Actor {
  private var boards = Map.empty[String,ActorRef]

  def receive = {
    case Join(name, mode) => {
      val board = boards.get(name) match {
        case None =>
          val b = context.actorOf(Props(new DrawingBoard(name)))
          b ! LoadBoard
          boards = boards + (name -> b)
          b
        case Some(b) => b
      }
      board ! JoinBoard(sender, mode)
    }
  }
}

case class Join(name: String, mode: String)

case class JoinBoard(client: ActorRef, mode: String)
case object SaveBoard
case object LoadBoard

case class Playback(id: Int)
case class Quit(id: Int)
case class ClientAction(id: Int, js: JsValue)
case class Connected(board: ActorRef, id: Int, enumerator:Enumerator[JsValue])
