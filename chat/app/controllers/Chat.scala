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
  
  def index = Action {
    Ok(views.html.index())
  }

  def join = Action {
    AsyncResult {
      implicit val timeout = Timeout(5.seconds)
      (new AkkaFuture((PersonActor.ref ? PersonActor.Join()).mapTo[Enumerator[String]])).asPromise.map { stream =>
        Ok.feed(stream &> EventSource[String]()).as(EVENT_STREAM)
      }
    }
  }

  def quit = Action {
    Ok
  }

  def newChild(name: String) = Action {
    PersonActor.ref ! PersonActor.NewChild(name)
    Ok("Child " + name + " born!")
  }
  
}