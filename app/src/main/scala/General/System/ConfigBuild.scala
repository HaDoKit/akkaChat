package General.System

import com.typesafe.config.{Config, ConfigFactory}
// создание Конфигурации Akka, можно использовать для видоизменения application.conf
object ConfigBuild {
    def apply(port : Int): Config = {
        ConfigFactory.parseString(
        s"""
#config-seeds
akka {
actor {
provider = "akka.cluster.ClusterActorRefProvider"
serializers {
    jackson-json = "akka.serialization.jackson.JacksonJsonSerializer"
}
serialization-bindings {
    "Event.SessionEvent" = jackson-json
}
}
remote.artery {
canonical {
  hostname = "127.0.0.1"
  port = $port
}
}

cluster {
seed-nodes = [
  "akka://General@127.0.0.1:2551",
  "akka://General@127.0.0.1:2552",
  "akka://General@127.0.0.1:2553",
  "akka://General@127.0.0.1:2554",
  "akka://General@127.0.0.1:2555",
  "akka://General@127.0.0.1:2556",
  "akka://General@127.0.0.1:2557"]

downing-provider-class = "akka.cluster.sbr.SplitBrainResolverProvider"
}
}
#config-seeds
 """)
    }
}
