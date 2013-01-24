package aspects

import forest._

trait ChatUpdate extends ir.Chat { this: TreeManipulation with Models =>
  trait ChatUpdate extends Chat {
    def updateChatRoom(chatRoom: Rep[NodeRef], msg: Rep[Message]): Rep[Unit] = {
      infix_transform(chatRoom, { n =>
        n.find(".messages").append(message(msg))
      })
    }
  }
}
