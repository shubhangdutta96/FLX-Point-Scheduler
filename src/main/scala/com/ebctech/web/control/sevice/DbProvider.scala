package com.ebctech.web.control.sevice

import slick.jdbc.JdbcBackend.Database

object DbProvider extends App{
  private var db: Database = null

  def getInstance(): Database = {
    if (this.db == null) {
      this.db =  Database.forConfig("database-config.db")
      print(db)
    }
    this.db
  }
  getInstance()
}