package com.ebctech.web.control.db.entity

import com.ebctech.web.control.actor._
import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._

class flxPointRecordTable(tags: Tag) extends Table[flxPointRecordEntity] (tags, "flxpoint_orders"){

  def order_number = column[String]("order_number")

  def order_request = column[String]("order_request")

  def place_order_response = column[Option[String]]("place_order_response")

  def response = column[Option[String]]("response")

  def order_number_vendor_id = column[String]("order_number_vendor_id")

  def status =column[String]("status")



  def *  = (order_number, order_request, place_order_response, response, order_number_vendor_id,status) <> (flxPointRecordEntity.tupled, flxPointRecordEntity.unapply)

}


