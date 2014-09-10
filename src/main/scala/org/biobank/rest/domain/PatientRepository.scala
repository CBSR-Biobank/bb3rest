package org.biobank.rest.domain

import org.biobank.rest.DbSession

import scala.slick.driver.MySQLDriver.simple._
import scala.slick.jdbc.{ GetResult, StaticQuery => Q }
import Q.interpolation
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import com.github.nscala_time.time.Imports._
import com.github.tototoshi.slick.MySQLJodaSupport._

object PatientRepository {

  implicit val session = DbSession.session

  implicit val specimenCountResult = GetResult(r => SpecimenCount(r.<<, r.<<))

  def specimenCounts(pnumber: String) = {
    val qryString = s"""SELECT specimen_type,count(*) as count
      |FROM specimen_webtable
      |WHERE pnumber = ?
      |GROUP BY specimen_type
      |ORDER BY specimen_type""".stripMargin

    val qry = Q.query[String, SpecimenCount](qryString)
    val counts = qry(pnumber).list
    PatientSpecimenCounts(pnumber, counts)
  }
}
