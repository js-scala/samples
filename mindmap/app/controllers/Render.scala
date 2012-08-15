package controllers

import play.api.mvc._
import play.api.http._
import play.api.mvc.Results._
import play.api.http.HeaderNames._

trait RenderSupport {
  def render[A](a: A)(implicit request: RequestHeader, repr: Render[A]): Result = repr.render(a)
}

/**
 * Defines how to render a resource of type `A` into an HTTP `Result` according to the `request` “Accept” header value
 */
trait Render[-A] {
  /** 
   * @return the best available representation of `a` according to the `request`
   */
  def render(a: A)(implicit request: RequestHeader): Result
}

/**
 * A representation for a value of type `A`
 * @param mimeType The MIME type of this representation
 * @param render Computation producing an HTTP result from a value of type `A`
 */
case class Repr[-A](mimeType: String, render: A => Result)

object Repr {
  implicit def fromTuple[A, B : Writeable : ContentTypeOf](a: (String, A => B)): Repr[A] =
    Repr(a._1, aa => Ok(a._2(aa)))
}

object Render {

  // The first "q" parameter (if any) separates the media-range parameter(s) from the accept-params.
  val qPattern = ";\\s*q=([0-9.]+)".r

  /**
   * @return A computation returning the quality factor associated with a given mime type according to the `request` “Accept” header.
   */
  def qualityFactor(request: RequestHeader): String => Option[Double] = request.headers.get(ACCEPT) match {
    case Some(acceptHeader) => {
      // list of pairs of (accepted-mime-type, quality-factor)
      val accepts = acceptHeader.split(",") map (_.trim) map { accept =>
        qPattern.findFirstMatchIn(accept) match {
          case Some(m) => (m.before, m.group(1).toDouble)
          case None => (accept, 1.0) // The default value is q=1.
        }
      }
      mimeType => accepts.find(_._1 == mimeType)
        // The asterisk "*" character is used to group media types into ranges, with "*/*" indicating all media types
        .orElse(accepts.find(_._1 == "*/*"))
        // and "type/*" indicating all subtypes of that type
        .orElse(accepts.find(_._1 == mimeType.takeWhile(_ != '/') + "/*"))
        .map(_._2)
    }
    // If no Accept header field is present, then it is assumed that the client accepts all media types.
    case None => _ => Some(1) // FIXME In which order will them be sorted?
  }

  def apply[A](r0: Repr[A], rs: Repr[A]*): Render[A] = new Render[A] {
    override def render(a: A)(implicit request: RequestHeader): Result = {
      // Get the quality factor computation for this request
      val qualityFactor = Render.qualityFactor(request)
      // Find the best available representation
      val (q, accepted) = (r0 +: rs).map(r => (qualityFactor(r.mimeType), r)).maxBy(_._1)
      q match {
        case Some(q) => accepted.render(a)
        // If an Accept header field is present, and if the server cannot send a response which is acceptable according
        // to the combined Accept field value, then the server SHOULD send a 406 (not acceptable) response.
        case None => NotAcceptable
      }
    }
  }

}