package javascripts

import virtualization.lms.common._
import js._

// Handle exploration of a mind map (zooming and moving)
trait Exploration extends JS with ExtraJS {
  type ViewState = JSLiteral { val s: Double; val tx: Double; val ty: Double }

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
      history.replaceState(new JSLiteral { val s = scale; val tx = translateX; val ty = translateY }, "", urlUpdate(scale, translateX, translateY))
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

trait ExternApi { this: Base with JSProxyBase =>
  type Extern[A] <: Rep[A]
  implicit def externToApi[A <: AnyRef : Manifest](f: Extern[A]): A = repProxy[A](f) // Han, all function calls will be considered effectful…
}

trait ExternApiExp extends ExternApi { this: BaseExp with JSProxyExp =>
  abstract class Extern[A : Manifest] extends Exp[A]
}

// --- Math JavaScript standard API

trait JSMath { this: Base with ExternApi with JSProxyBase =>
  trait Math {
    def round(x: Rep[Double]): Rep[Double]
  }
  val Math: Extern[Math]
}

trait JSMathExp extends JSMath { this: BaseExp with ExternApiExp with JSProxyExp =>
  object Math extends Extern[Math]
}

trait JSGenMath extends JSGenBase {
  val IR: BaseExp with JSMath
  import IR._

  override def quote(x: Exp[Any]) = x match {
    case Math => "Math"
    case _ => super.quote(x)
  }

}


// --- Option

trait OptionOps { this: Base =>
  class OptionOpsCls[+A : Manifest](o: Rep[Option[A]]) {
    def foreach(f: Rep[A] => Rep[Unit]) = option_foreach(o, f)
    def flatMap[B : Manifest](f: Rep[A] => Rep[Option[B]]) = option_flatMap(o, f)
  }
  implicit def repToOptionOpsCls[A : Manifest](o: Rep[Option[A]]): OptionOpsCls[A] = new OptionOpsCls(o)
  def option_foreach[A : Manifest](o: Rep[Option[A]], f: Rep[A] => Rep[Unit]): Rep[Unit]
  def option_flatMap[A : Manifest, B : Manifest](o: Rep[Option[A]], f: Rep[A] => Rep[Option[B]]): Rep[Option[B]]
}

trait OptionOpsExp extends OptionOps with EffectExp {
  def option_foreach[A : Manifest](o: Exp[Option[A]], f: Exp[A] => Exp[Unit]) = {
    val a = fresh[A]
    val block = reifyEffects(f(a))
    reflectEffect(OptionForeach(o, a, block), summarizeEffects(block).star)
  }
  def option_flatMap[A : Manifest, B : Manifest](o: Exp[Option[A]], f: Exp[A] => Exp[Option[B]]) = {
    val a = fresh[A]
    val block = reifyEffects(f(a))
    reflectEffect(OptionFlatMap(o, a, block), summarizeEffects(block).star)
  }

  case class OptionForeach[A : Manifest](o: Exp[Option[A]], a: Sym[A], block: Block[Unit]) extends Def[Unit]
  case class OptionFlatMap[A : Manifest, B](o: Exp[Option[A]], a: Sym[A], block: Block[Option[B]]) extends Def[Option[B]]

  override def syms(e: Any) = e match {
    case OptionForeach(o, _, block) => List(o, block).flatMap(syms)
    case OptionFlatMap(o, _, block) => List(o, block).flatMap(syms)
    case _ => super.syms(e)
  }

  override def boundSyms(e: Any) = e match {
    case OptionForeach(_, a, block) => a :: effectSyms(block)
    case OptionFlatMap(_, a, block) => a :: effectSyms(block)
    case _ => super.boundSyms(e)
  }

  override def symsFreq(e: Any) = e match {
    case OptionForeach(o, _, block) => List(o, block).flatMap(freqNormal)
    case OptionFlatMap(o, _, block) => List(o, block).flatMap(freqNormal)
    case _ => super.symsFreq(e)
  }

}

trait JSGenOptionOps extends JSGenEffect {
  val IR: EffectExp with OptionOpsExp
  import IR._

  override def emitNode(sym: Sym[Any], rhs: Def[Any]) = rhs match {
    case OptionForeach(o, a, block) =>
      stream.println("if (" + quote(o) + " !== null) {")
      emitValDef(a, quote(o)) // Ouin
      emitBlock(block)
      stream.println("}")
      emitValDef(sym, "undefined")
    case OptionFlatMap(o, a, block) =>
      emitValDef(a, quote(o))
      stream.println("if (" + quote(a) + " !== null) {")
      emitBlock(block)
      stream.println(quote(a) + " = " + getBlockResult(block) + ";")
      stream.println("}")
      emitValDef(sym, quote(a))
    case _ => super.emitNode(sym, rhs)
  }
}

// --- DOM

trait JSDom { this: Base =>

  trait EventTarget
  class EventTargetOps(t: Rep[EventTarget]) {
    def on[A <: EventDef](event: A, capture: Rep[Boolean] = unit(false))(handler: Rep[A#EventType] => Rep[Unit])(implicit m: Manifest[A#EventType]) = eventtarget_on(t, event, capture, handler)
  }
  implicit def repToEventTargetOps(t: Rep[EventTarget]): EventTargetOps = new EventTargetOps(t)
  def eventtarget_on[A <: EventDef](t: Rep[EventTarget], event: A, capture: Rep[Boolean], handler: Rep[A#EventType] => Rep[Unit])(implicit m: Manifest[A#EventType]): Rep[Unit]

  trait Event
  def infix_target(e: Rep[Event]): Rep[EventTarget]

  class EventDef(val name: String) {
    type EventType
  }

  trait PopStateEvent[A] extends Event
  def infix_state[A : Manifest](e: Rep[PopStateEvent[A]]): Rep[Option[A]]

  class PopState[A] extends EventDef("popstate") { type EventType = PopStateEvent[A] }
  object PopState {
    def apply[A] = new PopState[A]
  }

  trait MouseEvent extends Event
  def infix_offsetX(e: Rep[MouseEvent]): Rep[Double]
  def infix_offsetY(e: Rep[MouseEvent]): Rep[Double]

  trait MouseWheelEvent extends MouseEvent
  def infix_wheelDeltaY(e: Rep[MouseWheelEvent]): Rep[Double]

  object MouseWheel extends EventDef("mousewheel") { type EventType = MouseWheelEvent }
  object MouseDown extends EventDef("mousedown") { type EventType = MouseEvent }
  object MouseMove extends EventDef("mousemove") { type EventType = MouseEvent }
  object MouseUp extends EventDef("mouseup") { type EventType = MouseEvent }

  trait Window extends EventTarget

  val window: Rep[Window]
  def infix_document(w: Rep[Window]): Rep[Document]
  def infix_history(w: Rep[Window]): Rep[History]

  // Convenient aliases
  val document = window.document
  val history = window.history

  trait Document
  def infix_find(d: Rep[Document], selector: Rep[String]): Rep[Option[Element]]

  trait Element extends EventTarget
  def infix_setAttribute(e: Rep[Element], name: Rep[String], value: Rep[Any]): Rep[Unit]
  def infix_tagName(e: Rep[Element]): Rep[String]

  trait History
  class HistoryOps(h: Rep[History]) {
    def replaceState(state: Rep[_], title: Rep[String], url: Rep[String]) = history_replaceState(h, state, title, url)
  }
  implicit def repToHistoryOps(h: Rep[History]): HistoryOps = new HistoryOps(h)
  def history_replaceState(h: Rep[History], state: Rep[_], title: Rep[String], url: Rep[String]): Rep[Unit]

}

trait JSDomExp extends JSDom with EffectExp {
  def eventtarget_on[A <: EventDef](t: Exp[EventTarget], event: A, capture: Exp[Boolean], handler: Exp[A#EventType] => Exp[Unit])(implicit m: Manifest[A#EventType]) = {
    val e = fresh[A#EventType]
    val block = reifyEffects(handler(e))
    reflectEffect(EventTargetOn(t, event, capture, e, block))
  }
  def infix_target(e: Exp[Event]) = EventGetTarget(e)
  def infix_state[A : Manifest](e: Exp[PopStateEvent[A]]) = PopStateEventState(e)
  def infix_offsetX(e: Exp[MouseEvent]) = MouseEventOffsetX(e)
  def infix_offsetY(e: Exp[MouseEvent]) = MouseEventOffsetY(e)
  def infix_wheelDeltaY(e: Exp[MouseWheelEvent]) = MouseWheelEventDeltaY(e)
  def infix_document(w: Exp[Window]) = WindowDocument
  def infix_history(w: Exp[Window]) = WindowHistory
  def infix_find(d: Exp[Document], selector: Exp[String]) = DocumentFind(d, selector)
  def infix_setAttribute(e: Exp[Element], name: Exp[String], value: Exp[Any]) = reflectEffect(ElementSetAttribute(e, name, value))
  def infix_tagName(e: Exp[Element]) = ElementTagName(e)
  def history_replaceState(h: Exp[History], state: Exp[_], title: Exp[String], url: Exp[String]) = reflectEffect(HistoryReplaceState(h, state, title, url))
  object window extends Exp[Window]

  case class EventTargetOn[A <: EventDef](t: Exp[EventTarget], event: A, capture: Exp[Boolean], e: Sym[A#EventType], handler: Block[Unit]) extends Def[Unit]
  case class EventGetTarget(e: Exp[Event]) extends Def[EventTarget]
  case class PopStateEventState[A : Manifest](e: Exp[PopStateEvent[A]]) extends Def[Option[A]]
  case class MouseEventOffsetX(e: Exp[MouseEvent]) extends Def[Double]
  case class MouseEventOffsetY(e: Exp[MouseEvent]) extends Def[Double]
  case class MouseWheelEventDeltaY(e: Exp[MouseWheelEvent]) extends Def[Double]
  object WindowDocument extends Exp[Document]
  object WindowHistory extends Exp[History]
  case class DocumentFind(d: Exp[Document], selector: Exp[String]) extends Def[Option[Element]]
  case class ElementSetAttribute(e: Exp[Element], name: Exp[String], value: Exp[Any]) extends Def[Unit]
  case class ElementTagName(e: Exp[Element]) extends Def[String]
  case class HistoryReplaceState(h: Exp[History], state: Exp[_], title: Exp[String], url: Exp[String]) extends Def[Unit]

  override def syms(e: Any) = e match {
    case EventTargetOn(t, event, capture, _, handler) => List(t, event, capture, handler).flatMap(syms)
    case _ => super.syms(e)
  }

  override def boundSyms(e: Any) = e match {
    case EventTargetOn(_, _, _, e, handler) => e :: effectSyms(handler)
    case _ => super.boundSyms(e)
  }

  override def symsFreq(e: Any) = e match {
    case EventTargetOn(t, event, capture, _, handler) => List(t, event, capture, handler).flatMap(freqNormal)
    case _ => super.symsFreq(e)
  }

}

trait JSGenDom extends JSGenEffect {
  val IR: EffectExp with JSDomExp
  import IR._

  override def quote(x: Exp[Any]) = x match {
    case `window` => "window"
    case WindowDocument => "document"
    case WindowHistory => "history"
    case _ => super.quote(x)
  }

  override def emitNode(sym: Sym[Any], rhs: Def[Any]) = rhs match {
    case EventTargetOn(t, event, capture, e, handler) =>
      stream.println("var " + quote(sym) + " = " + quote(t) + ".addEventListener('" + event.name + "', function (" + quote(e) + ") {")
      emitBlock(handler)
      stream.println("}, " + quote(capture) + ");")
    case EventGetTarget(e) =>
      emitValDef(sym, quote(e) + ".target")
    case PopStateEventState(e) =>
      emitValDef(sym, quote(e) + ".state")
    case MouseEventOffsetX(e) =>
      emitValDef(sym, quote(e) + ".offsetX")
    case MouseEventOffsetY(e) =>
      emitValDef(sym, quote(e) + ".offsetY")
    case MouseWheelEventDeltaY(e) =>
      emitValDef(sym, quote(e) + ".wheelDeltaY")
    case DocumentFind(d, selector) =>
      emitValDef(sym, quote(d) + ".querySelector(" + quote(selector) + ")")
    case ElementSetAttribute(e, name, value) =>
      emitValDef(sym, quote(e) + ".setAttribute(" + quote(name) + ", " + quote(value) + ")")
    case ElementTagName(e) =>
      emitValDef(sym, quote(e) + ".tagName")
    case HistoryReplaceState(h, state, title, url) =>
      emitValDef(sym, quote(h) + ".replaceState(" + quote(state) + ", " + quote(title) + ", " + quote(url) + ")")
    case _ => super.emitNode(sym, rhs)
  }
}


trait ExtraJS extends JSMath with JSDom with OptionOps with ExternApi with JSProxyBase with Casts with LiftVariables with Variables
trait ExtraJSExp extends JSMathExp with JSDomExp with OptionOpsExp with ExternApiExp with JSProxyExp with CastsCheckedExp
trait JSGenExtra extends JSGenMath with JSGenDom with JSGenOptionOps with JSGenProxy with GenCastChecked {
  val IR: ExtraJSExp
}