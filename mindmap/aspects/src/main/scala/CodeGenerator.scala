
import forest._
import virtualization.lms.common._
import js._

object CodeGenerator extends App {

  trait Prog extends ir.Templates with views.Workspace with models.Models
  val prog = new Prog with ForestPkgExp with ScalaOpsPkgExp with StructExp with TupledFunctionsExp { context = Nil }
  val templates = new prog.Templates {}
  val jsProg = new javascripts.BuildMap with models.Models with JSExp with ListOpsExp with StructExp { context = Nil }

  val scalaGen = new ScalaGenForestPkg with ScalaCodeGenPkg with ScalaGenStruct { val IR: prog.type = prog; stream = null }
  val scalaOut = new java.io.PrintWriter("app/MindMap-generated.scala")
  scalaOut.println("package generated {")
  scalaGen.emitSource(templates.listMaps, "ListMaps", scalaOut)
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

  val jsGen = new JSGenForestPkg with JSCodeGenPkg with JSGenStruct { val IR: prog.type = prog; stream = null }
  val jsGen2 = new JSGen with JSGenListOps with JSGenStruct {
    val IR: jsProg.type = jsProg; stream = null
    import IR._
    def emitSource[A : Manifest](args: List[Sym[_]], body: Block[A], name: String, out: java.io.PrintWriter) = emitSourceAnyArity(args, body, name, out)
  }
  val jsOut = new java.io.PrintWriter("app/assets/javascripts/mindmap-generated.js")
  jsOut.println(";window.MindMap = (function (m) {")
  jsOut.print("m['showMap'] = ")
  jsGen.emitSource(prog.showMap, "", jsOut)
  jsOut.println(";")
  jsOut.print("m['buildMap'] = ")
  jsGen2.emitSource(jsProg.buildMap, "", jsOut)
  jsOut.println(";")
  jsOut.println("return m")
  jsOut.println("})(window.MindMap || {});")
  jsOut.close()
}