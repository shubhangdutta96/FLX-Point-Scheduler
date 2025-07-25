package com.ebctech.web.control.db.entity

import com.ebctech.web.control.actor.flxPointPartEntity
import slick.jdbc.MySQLProfile.api._
import slick.lifted.Tag


class flxPointPartTable (tags: Tag) extends Table[flxPointPartEntity](tags, "flxpoint_vendor_item"){

  def pk_order_item= column[String]("pk_order_item")

  def order_number= column[String]("order_number")

  def order_number_vendor_id = column[String]("order_number_vendor_id")

  def part_number = column[String]("part_number")

  def status = column[String]("status")

  def tracking_number =  column[String]("tracking_number")

  def * = (pk_order_item, order_number, order_number_vendor_id, part_number,status ,tracking_number) <> (flxPointPartEntity.tupled, flxPointPartEntity.unapply)

}

