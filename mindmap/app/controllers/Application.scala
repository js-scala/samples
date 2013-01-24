package controllers

import play.api._
import play.api.mvc._

trait Workspace {
  def index: Action[AnyContent]
  def show(id: String, vs: ViewSettings): Action[AnyContent]
}

object Application extends Controller {

  val delegate: Workspace = {
    import Play.current
    Play.configuration.getString("variant") match {
      case Some("v1") => new V1
      case Some("v2") => new V2
      case Some("v3") => new V3
      case Some("v4") => new V4
      case _ => sys.error("Oops. Did you forget to define the variant key configuration?")
    }
  }

  def index = delegate.index

  def create = TODO

  def show(id: String, vs: ViewSettings) = delegate.show(id, vs)

  def update(id: String) = TODO

}