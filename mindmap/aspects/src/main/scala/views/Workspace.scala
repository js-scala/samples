package views

import forest._
import scala.xml.Node
import scala.virtualization.lms.common._
import models._

trait Workspace extends ForestPkg with ScalaOpsPkg with LiftScala { this: Models =>

  def showMap(map: Rep[MindMap]): Rep[List[Node]] = {
    val defs = el('defs)(
      /*tag("marker", "id"->"arrow", "viewbox"->"0 0 10 10", "orient"->"auto")(
        tag("polyline", "points"->"0,0 20,5 10,30")())*/
    )

    val vertices = for (vertex <- map.vertices) yield {
      withNamespace(NS.SVG) { implicit ns =>
        el('g, 'class->'vertex, 'transform->("translate("+vertex.posx+","+vertex.posy+")"))(
          el('rect, 'width->vertex.width, 'height->vertex.height)(),
          el('foreignObject, 'x->0, 'y->0, 'width->vertex.width, 'height->vertex.height)(
            el('p, 'style->("height: "+vertex.height+"px;"))(vertex.content)(NS.HTML)
          )
        )
      }
    }

    val edges = for (edge <- map.edges) yield {
      val x1 = edge.orig.posx + edge.orig.width / 2
      val y1 = edge.orig.posy + edge.orig.height / 2
      val x2 = edge.end.posx + edge.end.width / 2
      val y2 = edge.end.posy + edge.end.height / 2
      withNamespace(NS.SVG) { implicit ns =>
        el('g, 'class->'edge)(
          el('line, 'x1->x1, 'y1->y1, 'x2->x2, 'y2->y2)())
      }
    }

    edges ++ vertices
  }

}