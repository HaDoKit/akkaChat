package Event

// класс описывающий передачу сообщений через систему akka
// требует сериализации т.к. выходит из системы
trait SessionEvent

case class MessagePosted(user : String,
                         message : String,
                         chatName : String) extends SessionEvent

case class MemberUp(user : String, chatName : String ) extends SessionEvent

case class MemberDown(user : String, chatName : String ) extends SessionEvent

case class OfferToJoin(userNameTo : String,
                       chatName : String,
                       userNameFrom : String) extends SessionEvent

case class WhoIsTheres(user : String,chatName : String) extends SessionEvent
case class IAmHere(responcer : String, asking : String, chatName : String) extends SessionEvent
