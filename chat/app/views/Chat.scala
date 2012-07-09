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

    lazy val chatRoom = compile((new Chat {}).chatRoom).asInstanceOf[ChatRoom => String]

}