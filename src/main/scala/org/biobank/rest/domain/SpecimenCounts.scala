package org.biobank.rest.domain

import spray.json.{ JsonFormat, DefaultJsonProtocol }

case class SpecimenCount(specimenType: String, count: Int)

case class CenterSpecimenCounts(storageCenter: String, specimenTypeCounts: List[SpecimenCount])

case class PatientSpecimenCounts(pnumber: String, study: String, storageCenters: List[CenterSpecimenCounts])

object SpecimenCountsJsonProtocol extends DefaultJsonProtocol {
  implicit val specimenCountFormat = jsonFormat2(SpecimenCount)
  implicit val centerSpecimenCountsFormat = jsonFormat2(CenterSpecimenCounts)
  implicit val patientSpecimenCountsFormat = jsonFormat3(PatientSpecimenCounts)
}
