package views

import forest.lms._
import models._

trait ChatUpdate extends ir.Chat with TreeManipulation {
  implicit val mNodeRef = manifest[forest.lib.NodeRef].asInstanceOf[Manifest[NodeRef]]
  trait ChatUpdate extends Chat {
    def updateChatRoom(p: Rep[(NodeRef, Message)]) {
      val (chatRoom, message) = (p._1, p._2)
      chatRoom.transform { n =>
        n.find(".messages").append((new Chat {}).message(message))
      }
    }
  }
}
