package controllers

import play.api._
import play.api.mvc._
import play.api.libs.concurrent._

import models._
import models.repr._

object Application extends Controller {

  def index = TODO

  def create = TODO

  def show(id: String, vs: ViewSettings) = TODO

  def update(id: String) = TODO


  def AsyncAction(r: => Promise[Result]) = Action(AsyncResult(r))
  def AsyncAction(f: RequestHeader => Promise[Result]) = Action(r => AsyncResult(f(r)))

}