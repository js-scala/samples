package javascripts

import virtualization.lms.common._
import scala.js.language._
import scala.js.language.dom.Dom
import scala.js.exp._
import scala.js.exp.dom.DomExp
import scala.js.gen.js._
import scala.js.gen.js.dom.GenDom

// Handle exploration of a mind map (zooming and moving)
trait Exploration extends JS with ExtraJS {
  type ViewState = Record { val s: Double; val tx: Double; val ty: Double }

  def setup(ps: Rep[Double], px: Rep[Double], py: Rep[Double], urlUpdate: Rep[((Double, Double, Double)) => String]) = for {
    workspace <- document.find(".workspace")
    svg <- document.find("svg")
  } {
    var scale = ps
    var translateX = px
    var translateY = py
    // Update url and svg transformation according to the current view state
    val updateTransform = fun { (scale: Rep[Double], translateX: Rep[Double], translateY: Rep[Double]) =>
      workspace.setAttribute("transform", "scale(" + scale / 100.0 + ") translate(" + translateX + " " + translateY + ")")
      history.replaceState(new Record { val s = scale; val tx = translateX; val ty = translateY }, "", urlUpdate(scale, translateX, translateY))
    }

    // History events
    window.on(PopState[ViewState]) { e =>
      for (s <- e.state) {
        scale = s.s
        translateX = s.tx
        translateY = s.ty
        updateTransform(scale, translateX, translateY)
      }
    }

    // Zoom
    svg.on(MouseWheel) { e =>
      val newScale = Math.round(scale + e.wheelDeltaY / 16.0)
      if (newScale > 0.0) {
        translateX = Math.round(translateX - e.offsetX * 100.0 / scale + e.offsetX * 100.0 / newScale)
        translateY = Math.round(translateY - e.offsetY * 100.0 / scale + e.offsetY * 100.0 / newScale)
        scale = newScale
        updateTransform(scale, translateX, translateY)
      }
    }

    // Move
    var moving = false
    var x = 0.0
    var y = 0.0
    window.on(MouseDown) { e =>
      if (e.target.as[Element].tagName == "svg") {
        moving = true
        x = e.offsetX
        y = e.offsetY
      }
    }
    window.on(MouseMove) { e =>
      if (moving) {
        translateX = Math.round(translateX + (e.offsetX - x) * 100.0 / scale)
        translateY = Math.round(translateY + (e.offsetY - y) * 100.0 / scale)
        x = e.offsetX
        y = e.offsetY
        updateTransform(scale, translateX, translateY)
      }
    }
    window.on(MouseUp) { _ => moving = false }
  }
}


// --- Helper to integrate external API

trait ExternApi { this: Base with Proxy =>
  import language.{higherKinds, implicitConversions}
  type Extern[A] <: Rep[A]
  implicit def externToApi[A <: AnyRef : Manifest](f: Extern[A]): A = repProxy[A](f) // Han, all function calls will be considered effectful…
}

trait ExternApiExp extends ExternApi { this: BaseExp with ProxyExp =>
  abstract class Extern[A : Manifest] extends Exp[A]
}

// --- Math JavaScript standard API

trait JSMath { this: Base with ExternApi with Proxy =>
  trait Math {
    def round(x: Rep[Double]): Rep[Double]
  }
  val Math: Extern[Math]
}

trait JSMathExp extends JSMath { this: BaseExp with ExternApiExp with ProxyExp =>
  object Math extends Extern[Math]
}

trait JSGenMath extends GenBase {
  val IR: BaseExp with JSMath
  import IR._

  override def quote(x: Exp[Any]) = x match {
    case Math => "Math"
    case _ => super.quote(x)
  }
}

trait ExtraJS extends JSMath with Dom with OptionOps with ExternApi with Proxy with Casts with LiftVariables with Variables
trait ExtraJSExp extends JSMathExp with DomExp with OptionOpsExp with ExternApiExp with ProxyExp with CastsCheckedExp
trait JSGenExtra extends JSGenMath with GenDom with GenOptionOps with GenProxy with GenCastChecked {
  val IR: ExtraJSExp
}