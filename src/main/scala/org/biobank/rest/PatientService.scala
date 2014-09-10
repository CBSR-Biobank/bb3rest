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
  import system.dispatcher
  import PatientService._

  implicit val system = context.system
  val log = Logging(system, getClass)

  def receive = {
    case SpecimenCounts(pnumber) =>
      specimenCounts(pnumber)
      context.stop(self)
  }

  def specimenCounts(pnumber: String) = {
    log.info(s"request for specimen counts: $pnumber")

    import PatientJsonProtocol._
    import SprayJsonSupport._

    requestContext.complete(PatientRepository.specimenCounts(pnumber))
  }

}

