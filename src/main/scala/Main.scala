import akka.actor.{ActorSystem, Props}
import play.api.libs.json.{JsString, Json}

import scala.concurrent.duration._
import scala.util.Random

/**
  * Created by trozozti on 29/04/16.
  */
object Main extends App {
  val system = ActorSystem("RightnowSystem")
  val remotePath = "akka.tcp://MicroMongoSystem@127.0.0.1:2552/user/mongoactor"
  val actor = system.actorOf(Props(classOf[LookupActor], remotePath), "lookupActor")

  println("Started LookupSystem")

  import system.dispatcher
  system.scheduler.schedule(1.second, 1.second) {
    if (Random.nextInt(100) % 2 == 0)
      actor ! Json.obj(("gallery",JsString("test val")))
    else
      actor ! Json.obj(("product",JsString("test val")))
  }
}
