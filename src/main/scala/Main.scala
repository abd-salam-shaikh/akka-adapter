import akka.actor.{ActorSystem, Props}
import play.api.libs.json.{JsNumber, Json}

import scala.concurrent.duration._

/**
  * Created by trozozti on 29/04/16.
  */
object Main extends App {
  val system = ActorSystem("RightnowSystem")

  val microMongoPath = "akka.tcp://MicroMongoSystem@127.0.0.1:2552/user/mongo-actor"
  val mongoActor = system.actorOf(Props(classOf[LookupActor], microMongoPath), "mongoLookupActor")
  val elasticPath = "akka.tcp://MongosticSystem@127.0.0.1:2554/user/elastic-actor"
  val elasticActor = system.actorOf(Props(classOf[LookupActor], elasticPath), "elasticLookupActor")

  println("Started LookupSystem")

  import system.dispatcher

  var counter: BigDecimal = 0.0
  system.scheduler.schedule(1.second, 1.second) {
    counter = counter.+(1)
    val doc = Json.obj(("faq", JsNumber(counter)))

    mongoActor ! doc
    elasticActor ! doc
  }
}
