package org.biobank.rest.domain

import spray.json.{ JsonFormat, DefaultJsonProtocol }

case class SpecimenCount(specimenType: String, count: Int)

case class PatientSpecimenCounts(pnumber: String, specimenCounts: List[SpecimenCount])

object PatientJsonProtocol extends DefaultJsonProtocol {
  implicit val specimenCountFormat = jsonFormat2(SpecimenCount)
  implicit val patientSpecimenCountsFormat = jsonFormat2(PatientSpecimenCounts)
}
