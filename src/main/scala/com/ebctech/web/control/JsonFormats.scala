package com.ebctech.web.control

import com.ebctech.web.control.actor._
import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat

object JsonFormats {
  implicit val ShipToAddressInfoFormat: RootJsonFormat[ShipToAddressInfo] = jsonFormat10(ShipToAddressInfo)
  implicit val orderLineItemInfoFormat: RootJsonFormat[OrderItemInfo] = jsonFormat3(OrderItemInfo)
  implicit val orderRequestFormat: RootJsonFormat[PlaceOrderRequest] = jsonFormat3(PlaceOrderRequest)
  implicit val placeOrderResponse: RootJsonFormat[PlaceOrderResponse] = jsonFormat3(PlaceOrderResponse)
}
