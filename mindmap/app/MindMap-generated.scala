package generated {
/*****************************************
  Emitting Generated Code                  
*******************************************/
class ListMaps extends ((scala.collection.immutable.List[RecordStringRecordStringListRecordStringintintintintListRecordRecordStringintintintintRecordStringintintintint], java.lang.String)=>(scala.xml.Node)) {
def apply(x0:scala.collection.immutable.List[RecordStringRecordStringListRecordStringintintintintListRecordRecordStringintintintintRecordStringintintintint], x1:java.lang.String): scala.xml.Node = {
val x3 = "/"+x1
val x4 = x3+"/"
val x16 = x0.flatMap { x2 => 
val x7 = x2.content
val x8 = x7.name
val x9 = {xml.Text(x8)}
val x5 = x2.id
val x6 = x4+x5
val x11 = <a href={x6}>{x9}</a>
val x13 = <li>{x11}</li>
val x14 = List(x13)
x14
}
val x17 = List()
val x18 = x16 ::: x17
val x19 = <ul>{x18}</ul>
val x20 = {xml.Text("Create a ")}
val x21 = {xml.Text("new mind map")}
val x23 = <strong>{x21}</strong>
val x24 = {xml.Text(": ")}
val x25 = <input type={"text"}  placeholder={"Name"} />
val x27 = <p>{List(x20, x23, x24, x25)}</p>
val x29 = <div>{List(x19, x27)}</div>
x29
}
}
/*****************************************
  End of Generated Code                  
*******************************************/
/*****************************************
  Emitting Generated Code                  
*******************************************/
class ShowMap extends ((RecordStringListRecordStringintintintintListRecordRecordStringintintintintRecordStringintintintint)=>(scala.collection.immutable.List[scala.xml.Node])) {
def apply(x31:RecordStringListRecordStringintintintintListRecordRecordStringintintintintRecordStringintintintint): scala.collection.immutable.List[scala.xml.Node] = {
val x32 = <defs />
val x33 = x31.vertices
val x52 = (0).toString()
val x58 = x33.map{
x34 => 
val x41 = x34.width
val x43 = (x41).toString()
val x42 = x34.height
val x44 = (x42).toString()
val x45 = <rect width={x43}  height={x44} />
val x48 = x34.content
val x49 = {xml.Text(x48)}
val x46 = "height: "+x42
val x47 = x46+"px;"
val x51 = <p style={x47}>{x49}</p>
val x54 = <foreignObject x={x52}  y={x52}  width={x43}  height={x44}>{x51}</foreignObject>
val x35 = x34.posx
val x36 = "translate("+x35
val x37 = x36+","
val x38 = x34.posy
val x39 = x37+x38
val x40 = x39+")"
val x56 = <g class={"vertex"}  transform={x40}>{List(x45, x54)}</g>
x56
}
val x59 = x31.edges
val x87 = x59.map{
x60 => 
val x61 = x60.orig
val x62 = x61.posx
val x63 = x61.width
val x64 = x63 / 2
val x65 = x62 + x64
val x79 = (x65).toString()
val x66 = x61.posy
val x67 = x61.height
val x68 = x67 / 2
val x69 = x66 + x68
val x80 = (x69).toString()
val x70 = x60.end
val x71 = x70.posx
val x72 = x70.width
val x73 = x72 / 2
val x74 = x71 + x73
val x81 = (x74).toString()
val x75 = x70.posy
val x76 = x70.height
val x77 = x76 / 2
val x78 = x75 + x77
val x82 = (x78).toString()
val x83 = <line x1={x79}  y1={x80}  x2={x81}  y2={x82} />
val x85 = <g class={"edge"}>{x83}</g>
x85
}
val x88 = x87 ::: x58
x88
}
}
/*****************************************
  End of Generated Code                  
*******************************************/
/*****************************************
  Emitting Generated Code                  
*******************************************/
class MindMap extends ((java.lang.String, scala.collection.immutable.List[RecordStringintintintint], scala.collection.immutable.List[RecordRecordStringintintintintRecordStringintintintint])=>(RecordStringListRecordStringintintintintListRecordRecordStringintintintintRecordStringintintintint)) {
def apply(x90:java.lang.String, x91:scala.collection.immutable.List[RecordStringintintintint], x92:scala.collection.immutable.List[RecordRecordStringintintintintRecordStringintintintint]): RecordStringListRecordStringintintintintListRecordRecordStringintintintintRecordStringintintintint = {
val x93 = RecordStringListRecordStringintintintintListRecordRecordStringintintintintRecordStringintintintint(name = x90, vertices = x91, edges = x92)
x93
}
}
/*****************************************
  End of Generated Code                  
*******************************************/
/*****************************************
  Emitting Generated Code                  
*******************************************/
class MindMapR extends ((java.lang.String, RecordStringListRecordStringintintintintListRecordRecordStringintintintintRecordStringintintintint)=>(RecordStringRecordStringListRecordStringintintintintListRecordRecordStringintintintintRecordStringintintintint)) {
def apply(x94:java.lang.String, x95:RecordStringListRecordStringintintintintListRecordRecordStringintintintintRecordStringintintintint): RecordStringRecordStringListRecordStringintintintintListRecordRecordStringintintintintRecordStringintintintint = {
val x96 = RecordStringRecordStringListRecordStringintintintintListRecordRecordStringintintintintRecordStringintintintint(id = x94, content = x95)
x96
}
}
/*****************************************
  End of Generated Code                  
*******************************************/
/*****************************************
  Emitting Generated Code                  
*******************************************/
class Vertex extends ((java.lang.String, Int, Int, Int, Int)=>(RecordStringintintintint)) {
def apply(x97:java.lang.String, x98:Int, x99:Int, x100:Int, x101:Int): RecordStringintintintint = {
val x102 = RecordStringintintintint(height = x101, posy = x99, content = x97, posx = x98, width = x100)
x102
}
}
/*****************************************
  End of Generated Code                  
*******************************************/
/*****************************************
  Emitting Generated Code                  
*******************************************/
class Edge extends ((RecordStringintintintint, RecordStringintintintint)=>(RecordRecordStringintintintintRecordStringintintintint)) {
def apply(x103:RecordStringintintintint, x104:RecordStringintintintint): RecordRecordStringintintintintRecordStringintintintint = {
val x105 = RecordRecordStringintintintintRecordStringintintintint(orig = x103, end = x104)
x105
}
}
/*****************************************
  End of Generated Code                  
*******************************************/
case class RecordRecordStringintintintintRecordStringintintintint(orig: RecordStringintintintint, end: RecordStringintintintint)
case class RecordStringintintintint(height: Int, posy: Int, content: java.lang.String, posx: Int, width: Int)
case class RecordStringListRecordStringintintintintListRecordRecordStringintintintintRecordStringintintintint(name: java.lang.String, vertices: scala.collection.immutable.List[RecordStringintintintint], edges: scala.collection.immutable.List[RecordRecordStringintintintintRecordStringintintintint])
case class RecordStringRecordStringListRecordStringintintintintListRecordRecordStringintintintintRecordStringintintintint(id: java.lang.String, content: RecordStringListRecordStringintintintintListRecordRecordStringintintintintRecordStringintintintint)
}
package object models {
  object MindMap extends generated.MindMap
  type MindMap = generated.RecordStringListRecordStringintintintintListRecordRecordStringintintintintRecordStringintintintint
  object MindMapR extends generated.MindMapR
  type MindMapR = generated.RecordStringRecordStringListRecordStringintintintintListRecordRecordStringintintintintRecordStringintintintint
  object Vertex extends generated.Vertex
  type Vertex = generated.RecordStringintintintint
  object Edge extends generated.Edge
  type Edge = generated.RecordRecordStringintintintintRecordStringintintintint
}
package object views {
  object listMaps extends generated.ListMaps
  object showMap extends generated.ShowMap
}
