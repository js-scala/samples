package mindmap

import java.io.{PrintWriter, File}

object Run extends App {
  val out = new PrintWriter("app/models/data-generated.scala")
  val source = new File("conf/maps")
  out.println("package models")
  out.println("object data {")
  Serializer(source.listFiles.toList.map(FreeMind.convert), out)
  out.println("}")
  out.close()
}