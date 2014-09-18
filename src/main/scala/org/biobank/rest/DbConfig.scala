package org.biobank.rest

import com.typesafe.config._
import scala.slick.jdbc.JdbcBackend.Database
import java.io.File
import com.mchange.v2.c3p0.ComboPooledDataSource

object DbConfig {

  case class DbConfigParams(host: String, name: String, user: String, password: String)

  val ConfigFileName = "db.conf"

  val ConfigPath = "db"

  val Driver = "com.mysql.jdbc.Driver"

  val conf = ConfigFactory.parseFile(new File(ConfigFileName)).resolve()

  if (!conf.hasPath(ConfigPath)) {
    println(s"\tError: settings not found in ${ConfigFileName}")
    System.exit(1)
  }

  val dbConf = conf.getConfig(ConfigPath);

  val dbConfigParams = DbConfigParams(
    dbConf.getString("host"),
    dbConf.getString("name"),
    dbConf.getString("user"),
    dbConf.getString("password"))

  val jdbcUrl = s"jdbc:mysql://${dbConfigParams.host}:3306/${dbConfigParams.name}?autoReconnect=true"

  val database = Database.forURL(
    jdbcUrl,
    driver   = Driver,
    user     = dbConfigParams.user,
    password = dbConfigParams.password)

  val databasePool = {
    val ds = new ComboPooledDataSource
    ds.setDriverClass(Driver)
    ds.setJdbcUrl(jdbcUrl)
    ds.setUser(dbConfigParams.user)
    ds.setPassword(dbConfigParams.password)
    ds.setInitialPoolSize(5)
    ds.setMinPoolSize(5)
    ds.setMaxPoolSize(15)
    ds.setCheckoutTimeout(1000)
    ds.setAcquireIncrement(5)
    Database.forDataSource(ds)
  }
}
