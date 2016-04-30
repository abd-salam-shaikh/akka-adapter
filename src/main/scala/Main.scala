import akka.actor.{ActorSystem, Props}
import play.api.libs.json.{JsNumber, Json}

import scala.concurrent.duration._
import scala.util.Random

/**
  * Created by trozozti on 29/04/16.
  */
object Main extends App {
  val system = ActorSystem("RightnowSystem")
  val microMongoPath = "akka.tcp://MicroMongoSystem@127.0.0.1:2552/user/mongoactor"
  val elasticPath = "akka.tcp://MongosticSystem@127.0.0.1:2554/user/elastic-actor"
  val mongoActor = system.actorOf(Props(classOf[LookupActor], microMongoPath), "mongoLookupActor")
  val elasticActor = system.actorOf(Props(classOf[LookupActor], elasticPath), "elasticLookupActor")

  println("Started LookupSystem")

  import system.dispatcher

  var counter: BigDecimal = 0.0
  system.scheduler.schedule(1.second, 1.second) {
    counter = counter.+(1)
    val doc = Json.obj(("faq", JsNumber(counter)))

    if (Random.nextInt(100) % 2 == 0) {
      mongoActor ! doc
      elasticActor ! doc
    }
    else {
      mongoActor ! doc
      elasticActor ! doc
    }
  }
}
