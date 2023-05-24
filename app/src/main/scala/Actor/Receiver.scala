package Actor

import Actor.Sender.IAmHeres
import Event._
import General.System.ActorSystems
import General.{Constants, SelfName}
import Room.Room
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import javafx.application.Platform
import javafx.concurrent._ // не понадобилось! использовал для этого runLater класса Platform

// класс получения сообщений от топика!
object Receiver {
    private var receiver : ActorRef[SessionEvent] = null

    def set(receiver : ActorRef[SessionEvent] ): Unit = {
      this.receiver = receiver
    }

    def get() : ActorRef[SessionEvent]  = {
      this.receiver
  }
    def apply() : Behavior[SessionEvent] = {
      Behaviors.setup {
        context =>
          Behaviors.receiveMessage {
            case MessagePosted(user, message,chatName) => {
              Platform.runLater( () => Room(chatName,ActorSystems.get()).getController().addMessage(message))
              Behaviors.same
            }
            case MemberUp(user,chatName) => {
              Platform.runLater(() => Room(chatName, ActorSystems.get()).getController().addUser(user))
              ActorSystems.get() ! IAmHeres(SelfName.getName(),user, chatName)
              Behaviors.same
            }
            case MemberDown(user,chatName) => {
              Platform.runLater( () => Room(chatName,ActorSystems.get()).getController().removeUser(user))
              Behaviors.same
            }
            case OfferToJoin(userNameTo, chatName, userNameFrom) => {
              if (userNameTo == SelfName.getName() || userNameFrom == SelfName.getName() ) {
                // показ комнаты пользователям!
                val generalRoom = Room(Constants.GeneralRoom, ActorSystems.get())
                Platform.runLater(() => generalRoom.getMainSceneController().newRoom(chatName))
              }
              Behaviors.same
            }
            // получение всех пользователей комнаты!
            case IAmHere(responcer,asking,chatName) => {
              if ( asking == SelfName.getName() && responcer != SelfName.getName()) {
                Platform.runLater( () => Room(chatName,ActorSystems.get()).getController().addUser(responcer))
             }
              Behaviors.same
            }
          }
      }
    }
}
