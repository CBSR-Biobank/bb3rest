package org.biobank.rest.domain

import spray.json.{ JsonFormat, DefaultJsonProtocol }

case class SpecimenType(specimenType: String)

case class StudySpecimenTypes(name: String, description: String, specimenTypes: List[SpecimenType])

case class PatientStudySpecimens(pnumber: String, study: StudySpecimenTypes)

object StudySpecimensJsonProtocol extends DefaultJsonProtocol {
  implicit val specimenTypeFormat = jsonFormat1(SpecimenType)
  implicit val studySpecimensFormat = jsonFormat3(StudySpecimenTypes)
  implicit val patientStudySpecimensFormat = jsonFormat2(PatientStudySpecimens)
}
