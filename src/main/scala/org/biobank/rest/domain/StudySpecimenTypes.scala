package org.biobank.rest.domain

import spray.json.{ JsonFormat, DefaultJsonProtocol }

case class StudySpecimenTypes(name: String, description: String, specimenTypes: List[String])

case class PatientStudySpecimens(pnumber: String, study: StudySpecimenTypes)

object StudySpecimensJsonProtocol extends DefaultJsonProtocol {
  implicit val studySpecimensFormat = jsonFormat3(StudySpecimenTypes)
  implicit val patientStudySpecimensFormat = jsonFormat2(PatientStudySpecimens)
}
