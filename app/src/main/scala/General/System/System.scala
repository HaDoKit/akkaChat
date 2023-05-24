package General.System

import Actor.Receiver
import Event.SessionEvent
import General.{Constants, SelfName}
import akka.actor.typed.pubsub.Topic
import akka.cluster.typed.Cluster

import scala.util.Random

object System {
  def apply(selfName : String) : Unit ={
    val port = 2551
    val configSystem1 = ConfigBuild(port)

    //устанавливаем имя!
    SelfName(selfName)

    // Обрабатываем только ошибку подключения к одному порту! В случае если ошибка произошла по другой причине,
    // Выкидываем ошибку
    // Не смог найти более изящный вариант автоматического создания нужной ноды!
    try {
      ActorSystems(configSystem1)
    } catch {
      case e: akka.remote.RemoteTransportException => {
        //Повышаем отказоустойчивость Кластера, запуская дополнительные исходные точки!
          val configSystem1 = ConfigBuild(Random.between(2551, 2557))
        try
          ActorSystems(configSystem1)
        catch {
          case e: akka.remote.RemoteTransportException => {
              val configSystem1 = ConfigBuild(0)
            ActorSystems(configSystem1)
          }
        }
      }
    }

    val cluster = Cluster(ActorSystems.get())

    // Создаём общую комнату в которую кидаем всех!
    val generalRoom = ActorSystems.get().systemActorOf(Topic[SessionEvent](Constants.GeneralRoom), Constants.GeneralRoom)

    //устанавливаем получателя сообщений!
    Receiver.set(ActorSystems.get().systemActorOf(Receiver(), SelfName.getName()))
    generalRoom ! Topic.subscribe(Receiver.get())
  }
}
