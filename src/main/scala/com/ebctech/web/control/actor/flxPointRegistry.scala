package com.ebctech.web.control.actor

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import com.ebctech.web.control.db.entity._
import com.ebctech.web.control.sevice.flxPointHandler.placeOrder
import slick.lifted.TableQuery

import scala.concurrent.Future

final case class OrderItemInfo(partNumber:String,
                               shippingMethod : String,
                               quantity:Int)

final case class ShipToAddressInfo(addressLine1: String,
                                   addressLine2: String,
                                   city: String,
                                   country: String,
                                   email :String,
                                   phoneNumber: String,
                                   postalCode: String,
                                   shipToName:String,
                                   stateProvince: String,
                                   altPhone : String)

final case class PlaceOrderRequest(orderNumber: String,
                                   lineItems: List[OrderItemInfo],
                                   shipToAddress: ShipToAddressInfo)

final case class PlaceOrderResponse(
                                     status: Int,
                                     responseDetail: String,
                                     orderId: String
                                   )

case class flxPointRecordEntity(order_number: String, order_request: String, place_order_response: Option[String], response: Option[String], order_number_vendor_id: String, status: String)

case class flxPointPartEntity(pk_order_item: String,
                         order_number: String,
                         order_number_vendor_id: String,
                         part_number: String,
                         status: String,
                         tracking_number: String)

object flxPointPartService extends TableQuery(new flxPointPartTable(_))
object flxPointServiceQuery extends TableQuery(new flxPointRecordTable(_))

object FLXPointRegistry {

  sealed trait Query
  final case class PlaceOrder(request: PlaceOrderRequest, replyTo: ActorRef[Future[PlaceOrderResponse]]) extends Query

  def apply(implicit system: ActorSystem[_]): Behavior[Query] =
    Behaviors.receiveMessage {

      case PlaceOrder (request, replyTo) =>
        replyTo ! placeOrder(request)
        Behaviors.same
    }
}
