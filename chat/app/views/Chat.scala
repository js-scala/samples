package views

import scala.virtualization.lms.common.CompileScala
import forest.lms._
import models._

object Chat extends ir.Chat with ForestPkgExp with FieldsExp with CompileScala { Ir =>

  	compiler = null
  	context = Nil
    override val codegen = new ScalaGenForestPkg with ScalaGenFields { val IR: Ir.type = Ir }

    def generateJs() {
      val jsCodegen = new JSGenForestPkg with JSGenFields { val IR: Ir.type = Ir }
      jsCodegen.emitModule(module[Chat], "Chat", new java.io.PrintWriter("public/javascripts/views.js"))
    }

    private val irChat = new Chat {}
    lazy val chatRoom = compile(irChat.chatRoom).asInstanceOf[ChatRoom => String]
    lazy val login = compile((_: Exp[_]) => irChat.login()).asInstanceOf[Unit => String](())
    lazy val connectedUser = compile(irChat.connectedUser).asInstanceOf[String => String]

}