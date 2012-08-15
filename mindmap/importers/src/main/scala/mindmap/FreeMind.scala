package mindmap

import models._
import java.io.{InputStream, File}
import scala.xml._

object FreeMind {
  def convert(input: File): MindMap = {

    val WIDTH_OF_M = 15
    val HEIGHT_OF_M = 21
    val H_GAP = 20
    val V_GAP = 15
    val LENGTH_TEXT = 22

    val map = XML.loadFile(input)

    def convertInternal(node: Node, x: Int, y: Int): (Vertex, List[Vertex], List[Edge], Int) = {
      val content = (node \ "@TEXT").head.text
      val width = math.min(content.size, LENGTH_TEXT) * WIDTH_OF_M
      val childX = x + (width + H_GAP)
      val (children, allVertices, allEdges, bottom) = (node \ "node").foldLeft(List.empty[Vertex], List.empty[Vertex], List.empty[Edge], y) {
        case ((childrenAcc, allVertices, allEdges, childY), child) => {
          val (vertex, vertices, edges, bottom) = convertInternal(child, childX, childY)
          (vertex +: childrenAcc, vertices ++ allVertices, edges ++ allEdges, bottom)
        }
      }
      val height = ((content.size + LENGTH_TEXT - 1) / LENGTH_TEXT) * HEIGHT_OF_M
      val vertex = Vertex(content, x, (y + bottom) / 2, width, height)
      val edges = (for (child <- children) yield Edge(vertex, child))
      (vertex, vertex +: allVertices, edges ++ allEdges, math.max(bottom, vertex.posy + vertex.height + V_GAP))
    }

    val (root, vertices, edges, _) = convertInternal((map \ "node").head, 0, 0)

    MindMap(root.content, vertices, edges)
  }
}