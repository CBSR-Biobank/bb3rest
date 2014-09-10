package org.biobank.rest.domain

case class SpecimenCount(specimenType: String, count: Int)

case class PatientSpecimenCounts(pnumber: String, specimenCounts: Set[SpecimenCount])
