package controllers

import play.api.mvc._

trait AuthenticationSupport extends Controller { this: AuthenticationSettings =>

  def Authenticated(f: String => Request[_] => Result) = Action { implicit request =>
    session.get(Authentication.USERNAME) match {
      case Some(username) => f(username)(request)
      case None => Redirect(loginUrl)
    }
  }

}

trait Authentication extends Controller { this: AuthenticationSettings =>

  import play.api.data._
  import play.api.data.Forms._

  val authForm = Form("username" -> nonEmptyText)

  def login = Action {
    Ok(views.html.login(authForm))
  }

  def authenticate = Action { implicit request =>
    authForm.bindFromRequest.fold(
      badForm => BadRequest(views.html.login(badForm)),
      username => Redirect(authenticatedUrl).withSession(session + (Authentication.USERNAME -> username))
    )
  }

  def logout = Action { implicit request =>
    Redirect(logoutUrl).withSession(session - Authentication.USERNAME)
  }

}

trait AuthenticationSettings {
  def loginUrl: String
  def authenticatedUrl: String
  def logoutUrl: String
}

object Authentication {
  val USERNAME = "username"
}