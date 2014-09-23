package org.biobank.rest.domain

import org.biobank.rest.DbConfig

import scala.slick.driver.MySQLDriver.simple._
import scala.slick.jdbc.{ GetResult, StaticQuery => Q }
import Q.interpolation
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import com.github.nscala_time.time.Imports._
import com.github.tototoshi.slick.MySQLJodaSupport._
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object PatientRepository {

  val Log = LoggerFactory.getLogger(this.getClass)
  /** Returns the spcimen counts for a patient. The patient's number is used to identify the patient.
    */
  def getStudy(pnumber: String): Option[Study] = {
    val qryString = s"""SELECT study.id, study.name_short, study.name
      |FROM patient
      |JOIN study on study.id=patient.study_id
      |WHERE pnumber = ?""".stripMargin

    DbConfig.databasePool.withSession { implicit session =>
      val qry = Q.query[String, (Int, String, String)](qryString)
      qry(pnumber).firstOption match {
        case Some(row) => Some(Study(row._1, row._2, row._3))
        case None => None
      }
    }
  }

  /** Returns the spcimen counts for a patient. The patient's number is used to identify the patient.
    */
  def studySpecimenTypes(pnumber: String): Option[PatientStudySpecimens] = {
    getStudy(pnumber) match {
      case None => None
      case Some(study) => {
        val qryString = s"""SELECT stype.name_short
          |FROM study
          |JOIN aliquoted_specimen aspc ON study.id=aspc.study_id
          |JOIN specimen_type stype ON stype.id=aspc.specimen_type_id
          |WHERE study.id = ?
          |ORDER BY stype.name_short""".stripMargin

        DbConfig.databasePool.withSession { implicit session =>
          val qry = Q.query[Int, (String)](qryString)
          val specimenTypes = qry(study.id).list

          if (specimenTypes.isEmpty) {
            throw new IllegalStateException(s"could not find study aliquot types: $study.name")
          } else {
            // group by studies
            Some(PatientStudySpecimens(
              pnumber, StudySpecimenTypes(study.name, study.description, specimenTypes)))
          }
        }
      }
    }
  }

  /** Returns the spcimen counts for a patient. The patient's number is used to identify the patient.
    */
  def specimenCounts(pnumber: String): Option[PatientSpecimenCounts] = {
    getStudy(pnumber) match {
      case None => None
      case Some(study) => {
        val qryString = s"""SELECT center,specimen_type,count(*) as count
          |FROM specimen_webtable
          |WHERE pnumber = ?
          |GROUP BY center, specimen_type
          |ORDER BY center, specimen_type""".stripMargin

        DbConfig.databasePool.withSession { implicit session =>
          val qry = Q.query[String, (String, String, Int)](qryString)
          val counts = qry(pnumber).list

          if (counts.isEmpty) {
            None
          } else {
            // group by centers
            val centerMap = counts.groupBy(_._1).mapValues(_.map(x => SpecimenCount(x._2, x._3)))
            val centerSpecimenCounts = centerMap.map{ case (k,v) => CenterSpecimenCounts(k, v) }.toList
            Some(PatientSpecimenCounts(pnumber, study.name, centerSpecimenCounts))
          }
        }
      }
    }
  }

  /** Returns the spcimen counts for a patient. The patient's number is used to identify the patient.
    */
  def aliquots(pnumber: String): Option[PatientSpecimens] = {
    getStudy(pnumber) match {
      case None => None
      case Some(study) => {
        val qryString = s"""SELECT center,inventory_id,date_drawn,specimen_type,quantity
      |FROM specimen_webtable
      |WHERE pnumber = ?
      |ORDER BY date_drawn""".stripMargin

        val dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ")

        DbConfig.databasePool.withSession { implicit session =>
          val qry = Q.query[String, (String, String, DateTime, String, Option[BigDecimal])](qryString)
          val aliquots = qry(pnumber).list

          if (aliquots.isEmpty) {
            None
          } else {
            // group by centres
            val centersMap = aliquots.groupBy(_._1).mapValues(
              _.map(x => Specimen(x._2, dateFormat.print(x._3), x._4, x._5)))
            val patientSpecimens = centersMap.map{ case (k,v) => CenterSpecimens(k, v) }.toList
            Some(PatientSpecimens(pnumber, study.name, patientSpecimens))
          }
        }
      }
    }
  }
}
