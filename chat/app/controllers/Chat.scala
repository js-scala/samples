package controllers

import play.api._
import play.api.mvc._
import play.api.libs.EventSource

import play.api.libs.iteratee._
import play.api.libs.concurrent._

import akka.util.Timeout
import akka.util.duration._
import akka.pattern.ask

import actors.PersonActor

object Chat extends Controller {
  implicit val timeout = Timeout(5.seconds)
  
  def index = Action {
    AsyncResult {
      (new AkkaFuture((PersonActor.ref ? PersonActor.AllChildren).mapTo[String])).asPromise.map { allChildren =>
        Ok(views.html.index(allChildren))
      }
    }
  }

  def join = Action {
    import play.api.libs.json._
    def asJson: Enumeratee[String, JsValue] = Enumeratee.map { name =>
      JsObject(Seq("name"->JsString(name)))
    }
    AsyncResult {
      (new AkkaFuture((PersonActor.ref ? PersonActor.Join()).mapTo[Enumerator[String]])).asPromise.map { stream =>
        Ok.feed(stream &> asJson ><> EventSource[JsValue]()).as(EVENT_STREAM)
      }
    }
  }

  def quit = Action {
    Ok("You have been disconected")
  }

  def newChild(name: String) = Action {
    PersonActor.ref ! PersonActor.NewChild(name)
    Ok("Child " + name + " born!")
  }
  
}