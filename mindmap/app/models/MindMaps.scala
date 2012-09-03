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
    val flexibility = Vertex("Flexibility allows for procrastination", 100, 100, 260, 25)
    val procrastination = Vertex("Procrastination improves agility", 400, 175, 230, 25)
    val agility = Vertex("Agility gives more flexibility", 275, 300, 200, 25)
    val agilityMap = MindMap("Agility", List(flexibility, procrastination, agility), List(Edge(flexibility, procrastination), Edge(procrastination, agility), Edge(agility, flexibility)))
    val patternsMap = data.generated.last
    //val gs = for (map <- data.generated) yield Inserted(freshId(), map)
    Seq(Inserted("agility", agilityMap), Inserted("design-patterns", patternsMap))
  }


  import concurrent.stm.Ref

  val ref = Ref(MindMaps()).single

  def commit(e: Event) {
    ref.transform(_.apply(e))
  }

  init.foreach(commit)

}