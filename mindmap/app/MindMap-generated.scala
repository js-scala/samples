package generated {
/*****************************************
  Emitting Generated Code                  
*******************************************/
class ListMaps extends ((scala.collection.immutable.List[C1560200007])=>(scala.xml.Node)) {
def apply(x0:scala.collection.immutable.List[C1560200007]): scala.xml.Node = {
val x13 = x0.flatMap { x1 => 
val x4 = x1.content
val x5 = x4.name
val x6 = {xml.Text(x5)}
val x2 = x1.id
val x3 = "/"+x2
val x8 = <a href={x3}>{x6}</a>
val x10 = <li>{x8}</li>
val x11 = List(x10)
x11
}
val x14 = List()
val x15 = x13 ::: x14
val x16 = <ul>{x15}</ul>
val x17 = {xml.Text("Create a ")}
val x18 = {xml.Text("new mind map")}
val x20 = <strong>{x18}</strong>
val x21 = {xml.Text(": ")}
val x22 = <input type={"text"}  placeholder={"Name"} />
val x24 = <p>{List(x17, x20, x21, x22)}</p>
val x26 = <div class={"list-maps"}>{List(x16, x24)}</div>
x26
}
}
/*****************************************
  End of Generated Code                  
*******************************************/
/*****************************************
  Emitting Generated Code                  
*******************************************/
class ShowMap extends ((C1421686256)=>(scala.collection.immutable.List[scala.xml.Node])) {
def apply(x28:C1421686256): scala.collection.immutable.List[scala.xml.Node] = {
val x29 = <defs />
val x30 = x28.vertices
val x49 = (0).toString()
val x55 = x30.map{
x31 => 
val x38 = x31.width
val x40 = (x38).toString()
val x39 = x31.height
val x41 = (x39).toString()
val x42 = <rect width={x40}  height={x41} />
val x45 = x31.content
val x46 = {xml.Text(x45)}
val x43 = "height: "+x39
val x44 = x43+"px;"
val x48 = <p style={x44}>{x46}</p>
val x51 = <foreignObject x={x49}  y={x49}  width={x40}  height={x41}>{x48}</foreignObject>
val x32 = x31.posx
val x33 = "translate("+x32
val x34 = x33+","
val x35 = x31.posy
val x36 = x34+x35
val x37 = x36+")"
val x53 = <g class={"vertex"}  transform={x37}>{List(x42, x51)}</g>
x53
}
val x56 = x28.edges
val x84 = x56.map{
x57 => 
val x58 = x57.orig
val x59 = x58.posx
val x60 = x58.width
val x61 = x60 / 2
val x62 = x59 + x61
val x76 = (x62).toString()
val x63 = x58.posy
val x64 = x58.height
val x65 = x64 / 2
val x66 = x63 + x65
val x77 = (x66).toString()
val x67 = x57.end
val x68 = x67.posx
val x69 = x67.width
val x70 = x69 / 2
val x71 = x68 + x70
val x78 = (x71).toString()
val x72 = x67.posy
val x73 = x67.height
val x74 = x73 / 2
val x75 = x72 + x74
val x79 = (x75).toString()
val x80 = <line x1={x76}  y1={x77}  x2={x78}  y2={x79} />
val x82 = <g class={"edge"}>{x80}</g>
x82
}
val x85 = x84 ::: x55
x85
}
}
/*****************************************
  End of Generated Code                  
*******************************************/
/*****************************************
  Emitting Generated Code                  
*******************************************/
class MindMap extends ((java.lang.String, scala.collection.immutable.List[C979208137], scala.collection.immutable.List[C817727332])=>(C1421686256)) {
def apply(x87:java.lang.String, x88:scala.collection.immutable.List[C979208137], x89:scala.collection.immutable.List[C817727332]): C1421686256 = {
val x90 = C1421686256(name = x87, vertices = x88, edges = x89)
x90
}
}
/*****************************************
  End of Generated Code                  
*******************************************/
/*****************************************
  Emitting Generated Code                  
*******************************************/
class MindMapR extends ((java.lang.String, C1421686256)=>(C1560200007)) {
def apply(x91:java.lang.String, x92:C1421686256): C1560200007 = {
val x93 = C1560200007(id = x91, content = x92)
x93
}
}
/*****************************************
  End of Generated Code                  
*******************************************/
/*****************************************
  Emitting Generated Code                  
*******************************************/
class Vertex extends ((java.lang.String, Int, Int, Int, Int)=>(C979208137)) {
def apply(x94:java.lang.String, x95:Int, x96:Int, x97:Int, x98:Int): C979208137 = {
val x99 = C979208137(height = x98, posy = x96, content = x94, posx = x95, width = x97)
x99
}
}
/*****************************************
  End of Generated Code                  
*******************************************/
/*****************************************
  Emitting Generated Code                  
*******************************************/
class Edge extends ((C979208137, C979208137)=>(C817727332)) {
def apply(x100:C979208137, x101:C979208137): C817727332 = {
val x102 = C817727332(orig = x100, end = x101)
x102
}
}
/*****************************************
  End of Generated Code                  
*******************************************/
case class C1560200007(id: java.lang.String, content: C1421686256)
case class C1421686256(name: java.lang.String, vertices: scala.collection.immutable.List[C979208137], edges: scala.collection.immutable.List[C817727332])
case class C979208137(content: java.lang.String, posx: Int, posy: Int, width: Int, height: Int)
case class C817727332(orig: C979208137, end: C979208137)
}
package object models {
  object MindMap extends generated.MindMap
  type MindMap = generated.C1421686256
  object MindMapR extends generated.MindMapR
  type MindMapR = generated.C1560200007
  object Vertex extends generated.Vertex
  type Vertex = generated.C979208137
  object Edge extends generated.Edge
  type Edge = generated.C817727332
}
package object views {
  object listMaps extends generated.ListMaps
  object showMap extends generated.ShowMap
}
