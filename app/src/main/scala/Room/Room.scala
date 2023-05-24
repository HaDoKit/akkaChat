package Room

import Actor.Receiver
import Actor.Sender.{Commands, MemberUps}
import Event.SessionEvent
import General.Controller.{MainSceneController, RoomController}
import General.{Constants, SelfName}
import akka.actor.typed.pubsub.Topic
import akka.actor.typed.{ActorRef, ActorSystem}

import scala.collection.mutable.ArrayBuffer


// реализация комнат для передачи сообщений.
// нужно придумать как добавить основную комнату! Upd. Название главной комнаты всегда одно General!
// так же как создавать комнату при присоединении пользователя к ней! Upd. Индентично главной комнате!
object Room {
  private var rooms = ArrayBuffer[Room]()
  private var stringID = ArrayBuffer[String]()
    def apply(chatName : String, actorSystem : ActorSystem[Commands]): Room = {
      var room : Room = null
      val userTo = chatName.split("To")
      var check : Boolean = false
      var user: String = null
      var userFrom: String = null

      try {
        stringID(stringID.indexWhere(element => element == chatName)).isEmpty
      } catch  {
          case e : ArrayIndexOutOfBoundsException => check = true
          case e : IndexOutOfBoundsException => check = true
      }

      if ( check ) {
        try {
           user = userTo(1)
           userFrom = userTo(0)
        } catch {
          case e : ArrayIndexOutOfBoundsException => {
             user = Constants.GeneralRoom
             userFrom = Constants.GeneralRoom }
        }

        room = new Room(chatName,actorSystem,user,userFrom)
        stringID += chatName
        rooms += room
      } else {
        room = rooms(stringID.indexWhere(element => element == chatName))
      }
      room
    }
}

class Room(chatName : String, actorSystem : ActorSystem[Commands], userTo : String, userFrom : String) {

  private var room = actorSystem.systemActorOf(Topic[SessionEvent](chatName + "Room" ), chatName + "Room")

  private var roomController = new RoomController()
  private var mainSceneController = new MainSceneController()

  def getMainSceneController(): MainSceneController = {
    mainSceneController
  }
  def setMainSceneController(sceneController : MainSceneController) : Unit = {
    this.mainSceneController = sceneController
  }

  def getController() : RoomController = {
    roomController
  }

  def setController(roomController : RoomController) : Unit ={
    this.roomController = roomController
  }

  def chatName() : String = {
    chatName
  }
  def chatNameVisible() : String = {
    if (SelfName.getName() == userTo) {
      userFrom
    } else {
      userTo
    }
  }

  def subscribe(): Unit = {
      room ! Topic.subscribe(Receiver.get())
      //actorSystem ! WhoIsThere(SelfName.getName(),chatName())
    Thread.sleep(5000)
      actorSystem ! MemberUps(SelfName.getName(),room,chatName)
  }
  def getRoom() : ActorRef[Topic.Command[SessionEvent]] = {
      this.room
  }

}
