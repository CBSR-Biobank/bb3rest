package org.biobank.rest

import org.biobank.rest.domain._

import akka.actor.{Actor, ActorRef}
import akka.event.Logging
import akka.io.IO

import spray.routing.RequestContext
import spray.httpx.SprayJsonSupport

import scala.util.{ Success, Failure }

object PatientService {
  case class SpecimenCounts(pnumber: String)
}

class PatientService(requestContext: RequestContext) extends Actor {

  implicit val system = context.system
  import system.dispatcher
  val log = Logging(system, getClass)

  def receive = {
    case PatientService.SpecimenCounts(pnumber) =>
      specimenCounts(pnumber)
      context.stop(self)
  }

  def specimenCounts(pnumber: String) = {
    log.info(s"request for specimen counts: $pnumber")

    import PatientJsonProtocol._
    import SprayJsonSupport._

    val counts = Set(
      SpecimenCount("plasma", 4),
      SpecimenCount("blood", 5))
    requestContext.complete(PatientSpecimenCounts(pnumber, counts))
  }

}

