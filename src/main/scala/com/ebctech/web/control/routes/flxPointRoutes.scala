package com.ebctech.web.control.routes

import akka.actor.typed.scaladsl.AskPattern.{Askable, schedulerFromActorSystem}
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directives, Route}
import akka.util.Timeout
import com.ebctech.web.control.ApiVersion
import com.ebctech.web.control.actor.FLXPointRegistry.PlaceOrder
import com.ebctech.web.control.actor._

import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}

class flxPointRoutes(flxPointRegistry: ActorRef[FLXPointRegistry.Query])(implicit val system: ActorSystem[_]) extends SprayJsonSupport {
  import com.ebctech.web.control.JsonFormats._

  implicit val ec: ExecutionContext = system.executionContext
  implicit val timeout: Timeout = Timeout(3.seconds)

  def placeOrder(placeOrderRequest: PlaceOrderRequest):Future[PlaceOrderResponse] = flxPointRegistry.ask(PlaceOrder(placeOrderRequest, _)).flatten

  val flxPointRoutes: Route = pathPrefix(ApiVersion.pathPrefix) {
    concat(
      path("placeOrder") {
        post {
          entity(as[PlaceOrderRequest]) {placeOrderRequest =>
            rejectEmptyResponse {
              onSuccess(placeOrder(placeOrderRequest)) { response =>
                complete(response)
              }
            }
          }
        }
      },
      path("healthCheck") {
        get {
          complete("Service is running")
        }
      }
    )
  }
}
