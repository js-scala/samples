package controllers

import play.api.mvc._
import play.api.Play.current
import models.{MindMaps, MindMap}

class V4 extends Workspace with Controller {

  def index = Action {
    Ok(views.html.v4.index(MindMaps.ref().all))
  }

  def show(id: String, vs: ViewSettings) = Action { implicit request =>
    MindMaps.ref().find(id) match {
      case Some(map) => Ok(views.html.v4.show(play.api.cache.Cache.getOrElse("map_"+id+"_png", 60 * 60)(toPng(map, vs))))
      case None => NotFound
    }
  }

  // String representation, encoded in base64, of the PNG image of the rendered map
  def toPng(map: MindMap, vs: ViewSettings): String = {
    import org.apache.batik.transcoder._
    import org.apache.batik.transcoder.image.PNGTranscoder
    import java.io._
    val svg = <svg xmlns="http://www.w3.org/2000/svg" version="1.1">
      <g class="workspace" transform="scale({vs.s.map(_ / 100.0).getOrElse(1)}) translate({vs.x.getOrElse(0)} {vs.y.getOrElse(0)})">
        { views.showMap(map) }
      </g>
    </svg>
    val in = new StringReader(svg.toString)
    val out = new StringWriter
    (new PNGTranscoder).transcode(new TranscoderInput(in), new TranscoderOutput(out))
    new String(org.apache.commons.codec.binary.Base64.encodeBase64(out.toString.getBytes))
  }

}
