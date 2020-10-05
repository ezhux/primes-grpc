package lt.ezz

import akka.NotUsed
import akka.actor.typed.ActorSystem
import akka.grpc.GrpcClientSettings
import akka.http.scaladsl.model.ContentTypes._
import akka.http.scaladsl.model.{HttpEntity, HttpResponse}
import akka.http.scaladsl.model.StatusCodes.InternalServerError
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.ExceptionHandler
import akka.stream.scaladsl.Source
import akka.util.ByteString
import lt.ezz.primes.{PrimeReply, PrimeRequest, PrimesService, PrimesServiceClient}

import scala.concurrent.ExecutionContext
import scala.language.postfixOps
import scala.util.Try

class HttpRoutes(implicit executionContext: ExecutionContext, actorSystem: ActorSystem[Nothing]) {

  def mapToByteStream(method: PrimeRequest => Source[PrimeReply, NotUsed], upTo: Long) = {
    method(PrimeRequest(upTo))
      .map(_.primeNumber.toString)
      .map(s => ByteString(s + "\n"))
  }

  val exceptionHandler = ExceptionHandler {
    case ex: IllegalArgumentException => complete(HttpResponse(InternalServerError, entity = ex.getMessage))
  }

  val route =
    handleExceptions(exceptionHandler) {
      concat(
        path("prime" / Segment) { upTo =>
          get {
            require(Try { upTo.toLong }.isSuccess, "Argument must be a number" )
            require(upTo.toLong > 2, "Argument must be a number, bigger than 2")
            complete(HttpEntity(`text/plain(UTF-8)`, mapToByteStream(client.getPrimesUpTo, upTo.toLong)))
          }
        },
        path("naive" / LongNumber) { upTo =>
          get {
            complete(HttpEntity(`text/plain(UTF-8)`, mapToByteStream(client.getPrimesUpToNaive, upTo)))
          }
        },
        path("optimized" / LongNumber) { upTo =>
          get {
            complete(HttpEntity(`text/plain(UTF-8)`, mapToByteStream(client.getPrimesUpToOptimized, upTo)))
          }
        }
      )
    }

  val grpcClientSettings = GrpcClientSettings.fromConfig(PrimesService.name)
  val client: PrimesService = PrimesServiceClient(grpcClientSettings)

}