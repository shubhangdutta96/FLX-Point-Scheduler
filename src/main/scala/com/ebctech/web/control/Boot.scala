package com.ebctech.web.control

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.concat
import akka.http.scaladsl.server.Route
import com.ebctech.web.control.actor._
import com.ebctech.web.control.routes.flxPointRoutes

import scala.util.{Failure, Success}

object Boot {
  private def startHttpServer(routes: Route)(implicit system: ActorSystem[_]): Unit = {
    import system.executionContext

    val host = "localhost"
    val port = 8080

    val futureBinding = Http().newServerAt(host, port).bind(routes)
    futureBinding.onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        system.log.info("Server online at http://{}:{}/", address.getHostString, address.getPort)
      case Failure(ex) =>
        system.log.error("Failed to bind HTTP endpoint, terminating system", ex)
        system.terminate()
    }
  }

  def main(args: Array[String]): Unit = {
    val rootBehavior = Behaviors.setup[Nothing] { context =>
      val flxPointActor = context.spawn(FLXPointRegistry(context.system), "flxPointRegistryActor")

      context.watch(flxPointActor)

      val flxpointRoutes = new flxPointRoutes(flxPointActor)(context.system)

      startHttpServer(concat(flxpointRoutes.flxPointRoutes))(context.system)

      Behaviors.empty
    }
    ActorSystem[Nothing](rootBehavior, "TapsHttpServer")
  }

}
