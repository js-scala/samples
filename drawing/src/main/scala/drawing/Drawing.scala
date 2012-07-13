package drawing

import scala.js._
import scala.virtualization.lms.common._

import java.io.PrintWriter

trait MiceApi extends JSProxyBase with JSLiteral {
  val window: Rep[Any]

  type DataLiteral = JSLiteral {val data : String}
  def initWebSocket(): Rep[WebSocket]
  trait WebSocket {
    var onmessage: Rep[DataLiteral => Any]
    def send(data: Rep[String]): Rep[Unit]
  }
  implicit def repToSocket(x: Rep[WebSocket]): WebSocket = repProxy[WebSocket](x)

  type ActionLiteral = JSLiteral {val action: String; val id: String}
  type MoveLiteral = JSLiteral {val id: String; val cx: Int; val cy: Int; val w: Int; val h: Int; val color: String}

  def currentTime(): Rep[Int]

  val json: Rep[JSON]
  trait JSON {
    def stringify(literal: Rep[JSLiteral]): Rep[String]
    def parse[T <: JSLiteral](data: Rep[String]): Rep[T]
  }
  implicit def repToJSON(x: Rep[JSON]): JSON = repProxy[JSON](x)

  def jQuery(x: Rep[Any]): Rep[JQuery]
  trait JQuery {
    var length: Rep[Int]

    def width(): Rep[Int]
    def height(): Rep[Int]

    def append(s: Rep[String]): Rep[JQuery]
    def css(o: Rep[JSLiteral]): Rep[JQuery]
    def click(fn: Rep[JQueryEvent => Any]): Rep[JQuery]
    def mousemove(fn: Rep[JQueryEvent => Any]): Rep[JQuery]
    def remove(): Rep[JQuery]
  }
  implicit def repToJQuery(x: Rep[JQuery]): JQuery = repProxy[JQuery](x)
  type JQueryEvent = JSLiteral {val pageX: Int; val pageY: Int}
}

trait MiceApiExp extends MiceApi with JSProxyExp with JSLiteralExp with DomsExp {

  case object WindowVar extends Exp[Any]
  val window = WindowVar

  case object InitWebSocket extends Def[WebSocket]
  def initWebSocket() = reflectEffect(InitWebSocket)

  case object CurrentTime extends Def[Int]
  def currentTime() = reflectEffect(CurrentTime)

  case object JSONVar extends Exp[JSON]
  val json = JSONVar

  case class JQueryCall(x: Exp[Any]) extends Def[JQuery]
  def jQuery(x: Exp[Any]) = JQueryCall(x)
}

trait JSGenMiceApi extends JSGenProxy with JSGenLiteral {
  val IR: MiceApiExp
  import IR._

  override def quote(x: Exp[Any]) : String = x match {
    case WindowVar => "window"
    case JSONVar => "JSON"
    case _ => super.quote(x)
  }

  override def emitNode(sym: Sym[Any], rhs: Def[Any]) = rhs match {
    case InitWebSocket => emitValDef(sym,
      "new WebSocket(document.location.toString().replace('http://','ws://').replace('https://','wss://'))")
    case CurrentTime => emitValDef(sym,
      "(new Date()).getTime()")
    case JQueryCall(x) => emitValDef(sym,
      "$(" + quote(x) + ")")
    case _ => super.emitNode(sym, rhs)
  }
}

object Mice {
  trait DrawingProg extends MiceProg {
    override lazy val move = fun { (mouse: Rep[MoveLiteral]) =>
      val canvas = document.getElementById("canvas").as[Canvas]
      val c = canvas.getContext("2d")
      val x = (mouse.cx*6)/17
      val y = (mouse.cy*4)/17
      log(string_plus("(", string_plus(String.valueOf(x), string_plus(", ", string_plus(String.valueOf(y), ")")))))
      c.fillStyle = mouse.color
      c.fillRect(x, y, 5, 5)
    }
    override lazy val initialPen = unit(false)
    override lazy val togglePen = fun { penDown: Rep[Boolean] => !penDown }
  }

  trait CursorProg extends MiceProg {
    override lazy val move = fun { (mouse: Rep[MoveLiteral]) =>
      if (jQuery("#mouse_"+mouse.id).length == 0)
        jQuery("body").append("<span class='mouse' id='mouse_"+mouse.id+"'><span style='display:none;' class='chat'/></span>")
      jQuery("#mouse_"+mouse.id).css(new JSLiteral {
          val left = ((jQuery(window).width() - mouse.w) / 2 + mouse.cx) + "px"
          val top = mouse.cy + "px"
          val `background-color` = mouse.color
        })
      ()
    }
    override lazy val initialPen = unit(true)
    override lazy val togglePen = fun { penDown : Rep[Boolean] => penDown }
  }

  trait MiceProg extends JS with MiceApi with LiftVariables with Doms with JSDebug with Casts {
    val move: Rep[MoveLiteral => Unit]
    val initialPen: Rep[Boolean]
    val togglePen: Rep[Boolean => Boolean]
    def main(): Rep[Unit] = {
      var penDown = initialPen

      val ratelimit = fun { (ms: Rep[Int]) => fun { (fn: Rep[JQueryEvent => Any]) =>
          var last = currentTime()
          fun { (e: Rep[JQueryEvent]) =>
            val now = currentTime()
            if (now - last > ms) {
              last = now
              fn(e)
            }
          }
        }
      }

      val socket = initWebSocket()
      socket.onmessage = fun { (m: Rep[DataLiteral]) =>
        val data = json.parse(m.data).asInstanceOf[Rep[ActionLiteral]]
        if (data.action == "close")
          jQuery("#mouse_"+data.id).remove()
        else if (data.action == "move")
          move(data.asInstanceOf[Rep[MoveLiteral]])
      }

      jQuery(document).click { (e: Rep[JQueryEvent]) =>
        penDown = togglePen(penDown)
      }

      jQuery(document).mousemove {
        val r = ratelimit(40)
        r { (e: Rep[JQueryEvent]) =>
          if (penDown) {
            socket.send(json.stringify(new JSLiteral {
              val action = "move"
              val cx = e.pageX
              val cy = e.pageY
              val w = jQuery(window).width()
              val h = jQuery(window).height()
            }))
          }
        }
      }

      ()
    }
  }

  trait MiceProgExp extends MiceProg with JSExp with MiceApiExp with LiftVariables with DomsExp with JSDebugExp with Casts

  def codegenDrawing(pw: PrintWriter) {
    new DrawingProg with MiceProgExp { self =>
      val codegen = new JSGenOpt with JSGenMiceApi with GenDoms with JSGenDebug { val IR: self.type = self }
      codegen.emitSource0(main _, "main", pw)
    }
  }

  def codegenCursor(pw: PrintWriter) {
    new CursorProg with MiceProgExp { self =>
      val codegen = new JSGenOpt with JSGenMiceApi with GenDoms with JSGenDebug { val IR: self.type = self }
      codegen.emitSource0(main _, "main", pw)
    }
  }

 def writeJs(filename: String, codegen: PrintWriter => Unit) = {
   val out = new PrintWriter(filename)
   codegen(out)
   out.close()
 }
}

object Main extends App {
  Mice.writeJs("src/main/webapp/drawing_.js", Mice.codegenDrawing)
  Mice.writeJs("src/main/webapp/cursor_.js", Mice.codegenCursor)
}
