package controllers

case class ViewSettings(s: Option[Int], x: Option[Int], y: Option[Int])

object ViewSettings {
  import play.api.mvc.QueryStringBindable

  implicit def qsBinder: QueryStringBindable[ViewSettings] = new QueryStringBindable[ViewSettings] {
    override def bind(key: String, qs: Map[String, Seq[String]]) = try {
      def get(k: String): Option[String] = qs.get(k).flatMap(_.headOption)
      val s = get("s").map(_.toInt)
      val x = get("x").map(_.toInt)
      val y = get("y").map(_.toInt)
      Some(Right(ViewSettings(s, x, y)))
    } catch {
      case _ => Some(Left("Incorrect value"))
    }
    override def unbind(key: String, vs: ViewSettings) = {
      val s = vs.s.map(("s"->_))
      val x = vs.x.map(("x"->_))
      val y = vs.y.map(("y"->_))
      List(s, x, y).flatten.map({ case (k, v) => k+"="+v }).mkString("&")
    }
    override def javascriptUnbind = {
      """function(k,v){var a=[];v.s&&a.push('s='+v.s);v.x&&a.push('x='+v.x);v.y&&a.push('y='+v.y);return a.join('&')}"""
    }
  }
}