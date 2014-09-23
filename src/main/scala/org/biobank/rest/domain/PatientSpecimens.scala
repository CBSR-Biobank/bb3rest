package org.biobank.rest.domain

import spray.json.{ JsonFormat, DefaultJsonProtocol }


case class Specimen(inventoryId: String, dateDrawn: String, specimenType: String, quantity: Option[BigDecimal])

case class CenterSpecimens(storageCenter: String, specimens: List[Specimen])

case class PatientSpecimens(pnumber: String, storageCenters: List[CenterSpecimens])

object PatientSpecimensJsonProtocol extends DefaultJsonProtocol {
  implicit val specimenFormat = jsonFormat4(Specimen)
  implicit val centerSpecimensFormat = jsonFormat2(CenterSpecimens)
  implicit val patientSpecimensFormat = jsonFormat2(PatientSpecimens)
}

