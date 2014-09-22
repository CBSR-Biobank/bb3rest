package org.biobank.rest.domain

import spray.json.{ JsonFormat, DefaultJsonProtocol }

case class SpecimenType(specimenType: String)

case class StudySpecimens(name: String, description: String, specimenTypes: List[SpecimenType])

case class PatientStudySpecimens(pnumber: String, studySpecimens: StudySpecimens)

object StudySpecimensJsonProtocol extends DefaultJsonProtocol {
  implicit val specimenTypeFormat = jsonFormat1(SpecimenType)
  implicit val studySpecimensFormat = jsonFormat3(StudySpecimens)
  implicit val patientStudySpecimensFormat = jsonFormat2(PatientStudySpecimens)
}
