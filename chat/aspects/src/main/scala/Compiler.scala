import scala.virtualization.lms.common._
import forest._

object Compiler extends App {
  new aspects.updates.ChatUpdate with ForestPkgExp with TreeManipulationExp with FieldsExp { Ir =>
    context = Nil
    val out = new java.io.PrintWriter("app/Chat-generated.scala") // That’s very bad.
    out.println("package generated {")
    val scalaCodegen = new ScalaGenForestPkg with ScalaGenTreeManipulation with ScalaGenFields with ScalaGenTupleOps { val IR: Ir.type = Ir; stream = null }
    val irChat = new ChatUpdate {}
    scalaCodegen.emitSource(irChat.connectedUser, "ConnectedUser", out)
    scalaCodegen.emitSource(irChat.chatRoom, "ChatRoom", out)
    scalaCodegen.emitSource((_: Exp[Unit]) => irChat.login, "Login", out)
    scalaCodegen.emitSource(irChat.updateChatRoom, "UpdateChatRoom", out)
    out.println("}") // Close package generated
    out.println(
      """|package views {
         |  object connectedUser extends generated.ConnectedUser
         |  object chatRoom extends generated.ChatRoom
         |  object login extends generated.Login
         |}
         |package updates {
         |  object updateChatRoom extends generated.UpdateChatRoom
         |}""".stripMargin)
    out.close()

    val outJs = new java.io.PrintWriter("app/assets/javascripts/views.js") // Very bad, too.
    val jsCodegen = new JSGenForestPkg with JSGenTreeManipulation with JSGenFields { val IR: Ir.type = Ir; stream = null }
    outJs.println(";(function(){")
    jsCodegen.emitSource(irChat.connectedUser, "connectedUser", outJs)
    jsCodegen.emitSource((_: Exp[Unit]) => irChat.login, "login", outJs)
    jsCodegen.emitSource(irChat.updateChatRoom, "updateChatRoom", outJs)
    outJs.println(
      """|window.Chat = {
         |  views: {
         |    connectedUser: connectedUser,
         |    login: login
         |  },
         |  updates: {
         |    updateChatRoom: updateChatRoom
         |  }
         |};
         |""".stripMargin)
    outJs.println("})();")
    outJs.close()
  }
}