package views

import scala.virtualization.lms.common._
import forest._
import models._

object Chat extends ChatUpdate with ForestXmlPkgExp with TreeManipulationExp with FieldsExp with CompileScala { Ir =>

  	compiler = null
  	context = Nil
    override val codegen = new ScalaGenForestXmlPkg with ScalaGenTreeManipulation with ScalaGenFields with ScalaGenTupleOps { val IR: Ir.type = Ir; stream = null }

    def generateJs() {
      val jsCodegen = new JSGenForestPkg with JSGenTreeManipulation with JSGenFields { val IR: Ir.type = Ir; stream = null }
      jsCodegen.emitModule(module[ChatUpdate], "ChatUpdate", new java.io.PrintWriter("public/javascripts/views.js"))
      jsCodegen.emitModule(module[Chat], "Chat", new java.io.PrintWriter("public/javascripts/views2.js"))
    }

    private val irChat = new ChatUpdate {}
    lazy val chatRoom = compile(irChat.chatRoom)
    lazy val login = {
      val loginCompiled = compile((_: Exp[Unit]) => irChat.login())
      loginCompiled(())
    }
    lazy val connectedUser = compile(irChat.connectedUser)
    lazy val updateChatRoom = compile(irChat.updateChatRoom)

}