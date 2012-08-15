package models

import virtualization.lms.common._

trait Models extends Structs with ListOps with BooleanOps with LiftAll {

  type MindMap = Record { val name: String; val vertices: List[Vertex]; val edges: List[Edge] }
  type MindMapR = Record { val id: String; val content: MindMap }
  type Vertex = Record { val content: String; val posx: Int; val posy: Int; val width: Int; val height: Int }
  type Edge = Record { val orig: Vertex; val end: Vertex }

  def MindMap(n: Rep[String], vs: Rep[List[Vertex]], es: Rep[List[Edge]]): Rep[MindMap] = {
    new Record { val name = n; val vertices = vs; val edges = es }
  }
  def MindMapR(i: Rep[String], m: Rep[MindMap]): Rep[MindMapR] = {
    new Record { val id = i; val content = m }
  }
  def Vertex(c: Rep[String], px: Rep[Int], py: Rep[Int], w: Rep[Int], h: Rep[Int]): Rep[Vertex] = {
    new Record { val content = c; val posx = px; val posy = py; val width = w; val height = h }
  }
  def Edge(o: Rep[Vertex], e: Rep[Vertex]): Rep[Edge] = {
    new Record { val orig = o; val end = e }
  }

  def connections(vertex: Rep[Vertex], edges: Rep[List[Edge]]): Rep[List[Vertex]] = {
    for {
      edge <- edges
      if edge.orig == vertex || edge.end == vertex
    } yield {
      if (edge.orig == vertex) edge.end
      else edge.orig
    }
  }

  /*def addVertex(node: Rep[Vertex], map: Rep[MindMap]): Rep[MindMap] = {
    MindMap(map.id, map.name, node :: map.nodes, map.edges)
  }*/

  /*val connect = fun { (n1: Rep[Vertex], n2: Rep[Vertex], map: Rep[MindMap]) =>
    MindMap(map.id, map.name, map.nodes, Edge(n1, n2) :: map.edges)
  }*/

}
