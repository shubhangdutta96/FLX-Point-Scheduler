package com.ebctech.web.control.sevice

import akka.actor.typed.ActorSystem
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, StatusCodes}
import akka.http.scaladsl.unmarshalling.Unmarshal
import ch.qos.logback.classic.Logger
import com.ebctech.web.control.actor.{PlaceOrderRequest, PlaceOrderResponse, flxPointPartEntity, flxPointRecordEntity}
import com.ebctech.web.control.db.flxPointDbService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

import java.util.UUID
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object flxPointHandler {
  val dbService = new flxPointDbService()

  private final val logger = LoggerFactory.getLogger(this.getClass).asInstanceOf[Logger]

  def placeOrder(req: PlaceOrderRequest)(implicit system: ActorSystem[_]): Future[PlaceOrderResponse] = {
    val generatedOrderNumber = req.orderNumber /* + VENDOR_CODE */
    // HTTP API CALL
    /*
    private val config = ConfigFactory.load()
    config.getString("lkq.url")
   */
    /* val httpRequest = HttpRequest(
      method = HttpMethods.POST,
      uri = "https://external-api.com/placeOrder",
      entity = HttpEntity(ContentTypes.`application/json`, convertRequestToJson(req))
    )

    Http().singleRequest(httpRequest) */

    val savedOrderRequest = flxPointRecordEntity(req.orderNumber, "order_request", None, None, generatedOrderNumber, "")
    dbService.addOrderRequestToDB(savedOrderRequest)

    val resk = req.lineItems.toList
    resk.foreach{ i =>
      i.partNumber
      val save = flxPointPartEntity(UUID.randomUUID().toString, req.orderNumber,generatedOrderNumber, i.partNumber, "", "")
      dbService.addOrderToDB(save)
    }

    // Update DB after API call
    val jsonContent =
      """{
        |  "status": "success",
        |  "message": "Order placed successfully"
        |}""".stripMargin

    val response = HttpResponse(
      status = StatusCodes.OK,
      entity = HttpEntity(ContentTypes.`application/json`, jsonContent),
      headers = Seq(
        RawHeader("X-Custom-Header", "Sample")
      )
    )


    if (response.status.intValue() == 200) {
      Unmarshal(response.entity).to[String].flatMap { res =>
//        val jsonString = convertToJson(res)
        logger.info(s"$res")
        updateResponseToDb(req.orderNumber, res, Option(response.toString()), "Success")
        Future.successful(PlaceOrderResponse(response.status.intValue(), res, req.orderNumber))
      }
    } else {
      logger.info(s"Error in place Order")
      updateResponseToDb(req.orderNumber, "No response", Option(response.toString()), "Failed")
      Future.successful(PlaceOrderResponse(response.status.intValue(), response.toString(), req.orderNumber))
    }


  }

  private def updateResponseToDb(amPurchaseOrderNumber: String, Response: String, placeOrderResponse: Option[String], status: String): Unit = {
    val orderResponse = dbService.updateResponse(amPurchaseOrderNumber, Response, placeOrderResponse.getOrElse(""), status)
    orderResponse.onComplete {
      case Success(value) if value > 0 => logger.info("OrderResponse is updated successfully")
      case Success(value) => logger.info("OrderResponse is updated successfully")
      case Failure(exception) => logger.info(s"OrderResponse Not Updated $exception")
    }
  }

  @throws[Exception]
  def convertToJson(str: String): Option[String] = {
    try {
      val xmlMapper = new XmlMapper()
      val jsonMapper = new ObjectMapper()
      val tree = xmlMapper.readTree(str)
      val jsonString = jsonMapper.writeValueAsString(tree)
      Some(jsonString)
    } catch {
      case ex: Exception =>
        logger.error(s"Error converting XML to JSON: ${ex.getMessage}")
        None
    }
  }
}
