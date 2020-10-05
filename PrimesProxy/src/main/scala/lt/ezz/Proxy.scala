package lt.ezz

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http

import scala.io.StdIn
import scala.language.postfixOps

object Proxy {

  def main(args: Array[String]) = {

    implicit val system = ActorSystem(Behaviors.empty, "PrimesClient")
    implicit val executionContext = system.executionContext

    val httpConfig = system.settings.config.getConfig("akka.http.server")
    val host = httpConfig.getString("host")
    val port = httpConfig.getInt("port")

    val httpRoutes = new HttpRoutes()

    val bindingFuture = Http()
      .newServerAt(host, port).bind(httpRoutes.route)

    println(s"HTTP Server online at ${host}:${port}\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }

}