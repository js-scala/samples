import scala.virtualization.lms.common._
import forest._
import js._

object Compiler extends App {
  new aspects.ChatUpdate with ForestPkgExp with TreeManipulationExp with StructExp with StringOpsExp { Ir =>
    context = Nil
    val out = new java.io.PrintWriter("../app/Chat-generated.scala") // That’s very bad.
    out.println("package generated {")
    val scalaCodegen = new ScalaGenForestPkg with ScalaGenTreeManipulation with ScalaGenStruct with ScalaGenStringOps { val IR: Ir.type = Ir; stream = null }
    val irChat = new ChatUpdate {}
    scalaCodegen.emitSource(irChat.connectedUser, "ConnectedUser", out)
    scalaCodegen.emitSource(irChat.chatRoom, "ChatRoom", out)
    scalaCodegen.emitSource((_: Exp[Unit]) => irChat.login, "Login", out)
    scalaCodegen.emitSource2(irChat.updateChatRoom, "UpdateChatRoom", out)
    scalaCodegen.emitSource2(Ir.Message, "MessageM", out)
    scalaCodegen.emitSource(Ir.ChatRoom, "ChatRoomM", out)
    scalaCodegen.emitDataStructures(out)
    out.println("}") // Close package generated
    out.println(
      s"""|package views {
        |  object connectedUser extends generated.ConnectedUser
        |  object chatRoom extends generated.ChatRoom
        |  object login extends generated.Login
        |}
        |package updates {
        |  object updateChatRoom extends generated.UpdateChatRoom
        |}
        |package object models {
        |  type Message = generated.${scalaCodegen.remap(manifest[Ir.Message])}
        |  object Message extends generated.MessageM
        |  type ChatRoom = generated.${scalaCodegen.remap(manifest[Ir.ChatRoom])}
        |  object ChatRoom extends generated.ChatRoomM
        |}""".stripMargin)
    out.close()

    val outJs = new java.io.PrintWriter("../app/assets/javascripts/views.js") // Very bad, too.
    val jsCodegen = new JSGenForestPkg with JSGenTreeManipulation with JSGenStruct with JSGenStringOps { val IR: Ir.type = Ir; stream = null }
    outJs.println(";(function(){")
    jsCodegen.emitSource(irChat.connectedUser, "connectedUser", outJs)
    jsCodegen.emitSource((_: Exp[Unit]) => irChat.login, "login", outJs)
    jsCodegen.emitSource2(irChat.updateChatRoom, "updateChatRoom", outJs)
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