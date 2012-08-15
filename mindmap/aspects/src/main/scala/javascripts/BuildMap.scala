package javascripts

import models.Models
import virtualization.lms.common._
import js._

trait BuildMap extends JS with Structs { this: Models =>

  type FlatVertex = Vertex
  type FlatEdge = Record { val origIdx: Int; val endIdx: Int }
  type FlatMindMap = Record {
    val name: String
    val vertices: Array[FlatVertex]
    val edges: List[FlatEdge]
  }

  /*def buildMap(data: Rep[FlatMindMap]): Rep[MindMap] = {
    val edges = for {
      edge <- data.edges
      orig <- data.vertices.get(edge.origIdx)
      end <- data.vertices.get(edge.endIdx)
    } yield Edge(orig, end)
    MindMap(data.id, data.name, data.vertices, edges)
  }*/

  def buildMap(data: Rep[FlatMindMap]): Rep[MindMap] = {
    val vertices: ArrayOps[FlatVertex] = data.vertices
    val edges = for (edge <- data.edges) yield {
      val orig = vertices(edge.origIdx)
      val end = vertices(edge.endIdx)
      Edge(orig, end)
    }
    MindMap(data.name, vertices.toList, edges)
  }

}