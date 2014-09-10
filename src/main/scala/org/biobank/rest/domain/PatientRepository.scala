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

  /** Returns the spcimen counts for a patient. The patient's number is used to identify the patient.
    */
  def specimenCounts(pnumber: String) = {
    val qryString = s"""SELECT center,specimen_type,count(*) as count
      |FROM specimen_webtable
      |WHERE pnumber = ?
      |GROUP BY center, specimen_type
      |ORDER BY center, specimen_type""".stripMargin

    val qry = Q.query[String, (String, String, Int)](qryString)
    val counts = qry(pnumber).list

    // group by centers
    val centerMap = counts.groupBy(_._1).mapValues(_.map(x => SpecimenCount(x._2, x._3)))
    val centerSpecimenCounts = centerMap.map{ case (k,v) => CenterSpecimenCounts(k, v) }.toList
    PatientSpecimenCounts(pnumber, centerSpecimenCounts)
  }
}
