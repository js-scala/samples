
import forest._
import virtualization.lms.common._
import scala.js.exp.{JsScalaExp, JSExp}
import scala.js.gen.js.{GenJsScala => JSGenJsScala, GenJS}
import scala.js.gen.scala.{GenJsScala => ScalaGenJsScala}

object CodeGenerator extends App {

  trait Prog extends views.Workspace with models.Models
  val prog = new Prog with JsScalaExp with ForestExp { context = Nil }
  val jsProg = new javascripts.BuildMap with models.Models with JSExp with StructExp { context = Nil }
  val jsProg2 = new javascripts.Exploration with JSExp with javascripts.ExtraJSExp { context = Nil }

  val scalaGen = new ScalaGenJsScala with ScalaGenForest { val IR: prog.type = prog; stream = null }
  val scalaOut = new java.io.PrintWriter("../app/MindMap-generated.scala")
  scalaOut.println("package generated {")
  scalaGen.emitSource(prog.listMaps, "ListMaps", scalaOut)
  scalaGen.emitSource(prog.showMap, "ShowMap", scalaOut)
  scalaGen.emitSource3(prog.MindMap, "MindMap", scalaOut)
  scalaGen.emitSource2(prog.MindMapR, "MindMapR", scalaOut)
  scalaGen.emitSource5(prog.Vertex, "Vertex", scalaOut)
  scalaGen.emitSource2(prog.Edge, "Edge", scalaOut)
  scalaGen.emitDataStructures(scalaOut)
  scalaOut.println("}") // Close package “generated”
  scalaOut.println(
    """|package object models {
       |  object MindMap extends generated.MindMap
       |  type MindMap = generated.%s
       |  object MindMapR extends generated.MindMapR
       |  type MindMapR = generated.%s
       |  object Vertex extends generated.Vertex
       |  type Vertex = generated.%s
       |  object Edge extends generated.Edge
       |  type Edge = generated.%s
       |}
       |package object views {
       |  object listMaps extends generated.ListMaps
       |  object showMap extends generated.ShowMap
       |}""".stripMargin.format(
          scalaGen.remap(manifest[prog.MindMap]),
          scalaGen.remap(manifest[prog.MindMapR]),
          scalaGen.remap(manifest[prog.Vertex]),
          scalaGen.remap(manifest[prog.Edge])))
  scalaOut.close()

  val jsGen = new JSGenJsScala with JSGenForest { val IR: prog.type = prog; stream = null }
  val jsGen2 = new GenJS {
    val IR: jsProg.type = jsProg; stream = null
  }
  val jsGen3 = new GenJS with javascripts.JSGenExtra { val IR: jsProg2.type = jsProg2; stream = null }
  val jsOut = new java.io.PrintWriter("../app/assets/javascripts/mindmap-generated.js")
  jsOut.println(";window.MindMap = (function (m) {")
  jsOut.print("m['showMap'] = ")
  jsGen.emitSource(prog.showMap, "", jsOut)
  jsOut.println(";")
  jsOut.print("m['listMaps'] = ")
  jsGen.emitSource(prog.listMaps, "", jsOut)
  jsOut.println(";")
  jsOut.print("m['buildMap'] = ")
  jsGen2.emitSource(jsProg.buildMap, "", jsOut)
  jsOut.println(";")
  jsOut.print("m['setupExploration'] = ")
  jsGen3.emitSource4(jsProg2.setup, "", jsOut)
  jsOut.println(";")
  jsOut.println("return m")
  jsOut.println("})(window.MindMap || {});")
  jsOut.close()
}