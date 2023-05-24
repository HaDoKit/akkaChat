package Actor

import Event._
import General.SelfName
import General.System.ActorSystems
import Room.Room
import akka.actor.typed.pubsub.Topic
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import akka.cluster.typed._

// отправитель, получает команды для передачи сообщений.
// Не требует сериализации т.к. работает внутри системы
object Sender {
    trait Commands
    case class MessagePost(user : String ,
                           topic : ActorRef[Topic.Command[SessionEvent]],
                           message : String,
                           chatName : String
                          ) extends Commands

    case class MemberUps(user : String,
                         topic : ActorRef[Topic.Command[SessionEvent]],
                         chatName : String
                        ) extends Commands

    case class MemberDowns( user : String,
                            topic : ActorRef[Topic.Command[SessionEvent]],
                            chatName : String
                        ) extends Commands

    case class OfferToJoins(userName : String ,
                            topic : ActorRef[Topic.Command[SessionEvent]],
                            chatName : String
                           ) extends Commands
    case class WhoIsThere(asking : String, chatName : String) extends Commands
    case class IAmHeres(responcer : String , asking : String, chatName : String) extends Commands

      def apply() : Behavior[Commands] = {
        Behaviors.setup {
          context =>
            Behaviors.receiveMessage {
              case MessagePost(user,room, message,chatName) => {
                room ! Topic.publish(MessagePosted(user, message,chatName))
                Behaviors.same
              }
              case MemberUps(user,room,chatName) => {
                room ! Topic.publish(MemberUp(user, chatName))
                Behaviors.same
              }
              case MemberDowns(user,room,chatName) => {
                room ! Topic.publish(MemberDown(user, chatName))
                Behaviors.same
              }
              case OfferToJoins(userName,room, chatName) => {
                room ! Topic.publish(OfferToJoin(userName,chatName, SelfName.getName()))
                Behaviors.same
              }
              case WhoIsThere(user,chatName) => {
                Room(chatName,ActorSystems.get()).getRoom() ! Topic.publish(WhoIsTheres(user,chatName))
                Behaviors.same
              }
              case IAmHeres(responcer,asking,chatName) => {
                if (asking != SelfName.getName()) {
                  Room(chatName,ActorSystems.get()).getRoom() ! Topic.publish(IAmHere(responcer,asking,chatName))
                }
                Behaviors.same
            }
            }
        }
      }
}
