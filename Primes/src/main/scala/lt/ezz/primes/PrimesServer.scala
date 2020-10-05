package lt.ezz.primes

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.{Http, HttpConnectionContext}
import akka.stream.{ActorMaterializer, Materializer}
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.{ExecutionContext, Future}

object PrimesServer {
  def main(args: Array[String]): Unit = {
    // enable HTTP/2 in ActorSystem's config
    val conf = ConfigFactory
      .parseString("akka.http.server.preview.enable-http2 = on")
      .withFallback(ConfigFactory.defaultApplication())
    val system = ActorSystem("Primes", conf)
    new PrimesServer(system, conf).run()
  }
}

class PrimesServer(system: ActorSystem, conf: Config) {
  def run(): Future[Http.ServerBinding] = {

    implicit val sys: ActorSystem = system
    implicit val mat: Materializer = ActorMaterializer()
    implicit val ec: ExecutionContext = sys.dispatcher

    val host = conf.getString("akka.http.server.host")
    val port = conf.getInt("akka.http.server.port")

    val service: HttpRequest => Future[HttpResponse] =
      PrimesServiceHandler(new PrimesServiceImpl())

    val binding = Http().bindAndHandleAsync(
      service,
      interface = host,
      port = port,
      connectionContext = HttpConnectionContext())

    binding.foreach { binding => println(s"gRPC server bound to: ${binding.localAddress}") }

    binding
  }
}
