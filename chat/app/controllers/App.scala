package controllers

import play.api.mvc._

object App extends Controller with Chat with Authentication with AuthenticationSettings {
	val loginUrl = routes.App.login.url
	val authenticatedUrl = routes.App.index.url
	val logoutUrl = routes.App.index.url
}