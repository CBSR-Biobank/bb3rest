package org.biobank.rest.domain

import org.biobank.rest.DbConfig

import scala.slick.driver.MySQLDriver.simple._
import scala.slick.jdbc.{ GetResult, StaticQuery => Q }
import Q.interpolation
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import com.github.nscala_time.time.Imports._
import com.github.tototoshi.slick.MySQLJodaSupport._

object PatientRepository {


  /** Returns the spcimen counts for a patient. The patient's number is used to identify the patient.
    */
  def studySpecimens(pnumber: String) = {
    val qryString = s"""SELECT study.name_short, study.name, stype.name_short
      |FROM patient pt
      |JOIN study ON study.id=pt.study_id
      |JOIN aliquoted_specimen aspc ON study.id=aspc.study_id
      |JOIN specimen_type stype ON stype.id=aspc.specimen_type_id
      |WHERE pt.pnumber = ?
      |ORDER BY stype.name_short""".stripMargin

    DbConfig.databasePool.withSession { implicit session =>
      val qry = Q.query[String, (String, String, String)](qryString)
      val counts = qry(pnumber).list

      // group by studies
      val studiesMap = counts.groupBy(_._1).mapValues(_.map(x => SpecimenType(x._3)))
      if (studiesMap.size > 1) {
        throw new IllegalStateException("patient cannot have more than one study")
      }
      val studySpecimens = studiesMap.map{ case (k,v) => StudySpecimens(k, counts(0)._2, v) }.toList
      PatientStudySpecimens(pnumber, studySpecimens(0))
    }
  }

  /** Returns the spcimen counts for a patient. The patient's number is used to identify the patient.
    */
  def specimenCounts(pnumber: String) = {
    val qryString = s"""SELECT study,center,specimen_type,count(*) as count
      |FROM specimen_webtable
      |WHERE pnumber = ?
      |GROUP BY center, specimen_type
      |ORDER BY center, specimen_type""".stripMargin

    DbConfig.databasePool.withSession { implicit session =>
      val qry = Q.query[String, (String, String, String, Int)](qryString)
      val counts = qry(pnumber).list

      // group by centers
      val studiesMap = counts.groupBy(_._1).mapValues(_.map(x => (x._2, x._3, x._4)))
      if (studiesMap.size > 1) {
        throw new IllegalStateException("patient cannot have more than one study")
      }
      val centerMap = counts.groupBy(_._2).mapValues(_.map(x => SpecimenCount(x._3, x._4)))
      val centerSpecimenCounts = centerMap.map{ case (k,v) => CenterSpecimenCounts(k, v) }.toList
      PatientSpecimenCounts(pnumber, counts(0)._1, centerSpecimenCounts)
    }
  }
}
