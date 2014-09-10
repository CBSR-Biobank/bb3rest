package org.biobank.rest

import com.typesafe.config._
import scala.slick.jdbc.JdbcBackend.Database

case class DbConfig(host: String, name: String, user: String, password: String)

object DbSession {

  val conf = ConfigFactory.load()

  val dbConfig = DbConfig(
    conf.getString("dbhost"),
    conf.getString("dbname"),
    conf.getString("dbuser"),
    conf.getString("dbpassword"))

  val database = Database.forURL(
    s"jdbc:mysql://${dbConfig.host}:3306/${dbConfig.name}",
    driver   = "com.mysql.jdbc.Driver",
    user     = dbConfig.user,
    password = dbConfig.password)

  val session = database.createSession

}


