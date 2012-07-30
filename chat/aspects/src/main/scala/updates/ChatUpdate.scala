package aspects.updates

import forest._
import models._

trait ChatUpdate extends ir.Chat with TreeManipulation {
  trait ChatUpdate extends Chat {
    def updateChatRoom(p: Rep[(NodeRef, Message)]) {
      val (chatRoom, msg) = (p._1, p._2)
      chatRoom.transform { n =>
        n.find(".messages").append(message(msg).root)
      }
    }
  }
}
