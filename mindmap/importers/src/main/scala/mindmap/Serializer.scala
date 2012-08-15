package mindmap

import models._
import java.io.PrintWriter

object Serializer {

  def apply(data: List[MindMap], out: PrintWriter) {
    out.print("val generated = List(")
    for ((map, i) <- data.zipWithIndex) {
      this(map, out)
      if (i < data.size - 1) out.print(", ")
    }
    out.println(")")
  }

  def apply(map: MindMap, out: PrintWriter) {
    val freshId: () => Int = {
      var i = 0
      () => {
        i = i + 1
        i
      }
    }
    out.println("{")
    val verticesSeq = (for (vertex <- map.vertices) yield (vertex, freshId()))
    val verticesMap = verticesSeq.toMap
    for ((vertex, id) <- verticesSeq) {
      out.println("val x"+id+" = Vertex(\"\"\""+vertex.content+"\"\"\", "+vertex.posx+", "+vertex.posy+", "+vertex.width+", "+vertex.height+")")
    }
    out.println("MindMap(\"\"\""+map.name+"\"\"\", "+(for ((_, id) <- verticesSeq) yield "x"+id).mkString("List(", ", ", ")")+", "+(for (edge <- map.edges) yield "Edge(x"+verticesMap(edge.orig)+", x"+verticesMap(edge.end)+")").mkString("List(", ", ", ")")+")")
    out.println("}")
  }
}