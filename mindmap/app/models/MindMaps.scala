package models

sealed trait Event
case class Inserted(id: String, map: MindMap) extends Event

case class MindMaps(maps: Map[String, MindMap] = Map.empty) {

  def apply(e: Event): MindMaps = e match {
    case Inserted(id, map) => this.copy(this.maps + (id -> map))
  }

  def find(id: String): Option[MindMap] = maps.get(id)

  def all: Iterable[MindMapR] = for ((id, map) <- maps) yield MindMapR(id, map)

}

object MindMaps {

  val freshId = () => java.util.UUID.randomUUID().toString

  val init: Seq[Event] = {
    val hello = Vertex("Hello World!", 30, 30, 100, 25)
    val yo = Vertex("42", 40, 100, 30, 25)
    val i = Inserted("hello", MindMap("Hello", List(hello, yo), List(Edge(hello, yo))))
    val gs = for (map <- data.generated) yield Inserted(freshId(), map)
    i +: gs
  }

}