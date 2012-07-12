package views

import scala.virtualization.lms.common._
import forest.lms._
import models._

object Chat extends ChatUpdate with ForestPkgExp with TreeManipulationExp with FieldsExp with CompileScala { Ir =>

  	compiler = null
  	context = Nil
    override val codegen = new ScalaGenForestXmlPkg with ScalaGenTreeManipulation with ScalaGenFields with ScalaGenTupleOps { val IR: Ir.type = Ir }

    def generateJs() {
      val jsCodegen = new JSGenForestPkg with JSGenTreeManipulation with JSGenFields { val IR: Ir.type = Ir }
      jsCodegen.emitModule(module[ChatUpdate], "ChatUpdate", new java.io.PrintWriter("public/javascripts/views.js"))
      jsCodegen.emitModule(module[Chat], "Chat", new java.io.PrintWriter("public/javascripts/views2.js"))
    }

    private val irChat = new ChatUpdate {}
    lazy val chatRoom = compile(irChat.chatRoom).asInstanceOf[ChatRoom => scala.xml.Node]
    lazy val login = compile((_: Exp[_]) => irChat.login()).asInstanceOf[Unit => scala.xml.Node](())
    lazy val connectedUser = compile(irChat.connectedUser).asInstanceOf[String => scala.xml.Node]
    lazy val updateChatRoom = compile(irChat.updateChatRoom)

}