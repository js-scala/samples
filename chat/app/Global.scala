import play.api.GlobalSettings
import play.api.Application

object Global extends GlobalSettings {
  override def beforeStart(app: Application) {
    views.Chat.generateJs()
  }
}