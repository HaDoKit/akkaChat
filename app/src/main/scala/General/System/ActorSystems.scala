package General.System

import Actor.Sender
import Actor.Sender.Commands
import General.Constants
import akka.actor.typed.ActorSystem
import com.typesafe.config.Config


// вывел систему и актора системы в отдельный singleton для получения на любом уровне!
object ActorSystems {
  private var actorSystem : ActorSystem[Commands] = null

  def apply(config : Config) : Unit = {
      this.actorSystem = ActorSystem(Sender(),Constants.GeneralRoom, config)
    }

  def get() : ActorSystem[Commands] = {
    this.actorSystem
  }
}
