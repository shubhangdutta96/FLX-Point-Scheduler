package com.ebctech.web.control.db

import ch.qos.logback.classic.Logger
import com.ebctech.web.control.actor._
import com.ebctech.web.control.sevice.DbProvider
import org.slf4j.LoggerFactory
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Success

class flxPointDbService {
  private final val logger = LoggerFactory.getLogger(this.getClass).asInstanceOf[Logger]

  private final val db = DbProvider.getInstance()

  def checkOrderNumber(orderId: String): Future[Boolean] = {
    val query = flxPointServiceQuery.filter(_.order_number === orderId).exists.result
    db.run(query)
  }

  def addOrderRequestToDB(order: flxPointRecordEntity): Future[Int] = {
    val orderID = order.order_number

    val checkExistingOrder: Future[Boolean] = checkOrderNumber(orderID)
    checkExistingOrder.onComplete {
      case Success(existOrder) if existOrder =>
        logger.info(s"order Already Exist with orderID $orderID")
      case _ =>
        logger.info(s"order with orderID $orderID stored in DB")
        val insertAction = flxPointServiceQuery += order
        db.run(insertAction)
    }
    Future.successful(1)
  }

  def updateResponse(order_number:String,Response: String, placeOrderResponse: String, status: String): Future[Int] ={
    val query = flxPointServiceQuery.filter(_.order_number === order_number )
      .map(item => (item.response,item.place_order_response , item.status))
      .update(Some(Response), Some(placeOrderResponse) , status)

    db.run(query)
  }

  def addOrderToDB(order: flxPointPartEntity): Future[Int] = {
    val insertAction = flxPointPartService += order
    db.run(insertAction)
  }

}