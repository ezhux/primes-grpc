import java.util.concurrent.TimeUnit

import lt.ezz.HttpRoutes
import akka.http.scaladsl.testkit.{RouteTestTimeout, ScalatestRouteTest}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.model.StatusCodes

import scala.concurrent.duration.FiniteDuration

class ProxyTest extends AnyWordSpec with Matchers with ScalatestRouteTest {

  implicit val timeout = RouteTestTimeout(FiniteDuration(5, TimeUnit.SECONDS))
  implicit val actorSystem = ActorSystem(Behaviors.empty, "test-actor-system")

  val httpRoutes = new HttpRoutes()

  "Proxy" should {

    "handle wrong inputs" in {

      Get("/prime/abc") ~> httpRoutes.route ~> check {
        status shouldEqual StatusCodes.InternalServerError
        responseAs[String] shouldEqual "requirement failed: Argument must be a number"
      }

      Get("/prime/2") ~> httpRoutes.route ~> check {
        status shouldEqual StatusCodes.InternalServerError
        responseAs[String] shouldEqual "requirement failed: Argument must be a number, bigger than 2"
      }

    }

    "handle all correct paths (tests call the real PrimesCalculator, so it must be running)" in {
      Get("/prime/8") ~> httpRoutes.route ~> check {
        status shouldEqual StatusCodes.OK
        responseAs[String] shouldEqual "2\n3\n5\n7\n"
      }

      Get("/naive/8") ~> httpRoutes.route ~> check {
        status shouldEqual StatusCodes.OK
        responseAs[String] shouldEqual "2\n3\n5\n7\n"
      }

      Get("/optimized/8") ~> httpRoutes.route ~> check {
        status shouldEqual StatusCodes.OK
        responseAs[String] shouldEqual "2\n3\n5\n7\n"
      }
    }
  }

}
