package org.biobank.rest

import org.biobank.rest.domain._

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._
import spray.json.DefaultJsonProtocol

// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class PatientServiceActor extends Actor with PatientService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(patientRoute)
}

object JsonImplicits extends DefaultJsonProtocol {
  implicit val specimenCountJsonFormat = jsonFormat2(SpecimenCount)
  implicit val patientSpecimenCountsJsonFormat = jsonFormat2(PatientSpecimenCounts)
}

// this trait defines our service behavior independently from the service actor
trait PatientService extends HttpService {

  val patientRoute = {
    import spray.httpx.SprayJsonSupport.sprayJsonMarshaller
    import spray.httpx.SprayJsonSupport.sprayJsonUnmarshaller
    import JsonImplicits._

    pathPrefix("patients") {
      path(Segment) { pnumber: String =>
        get {
          complete {
            "Received GET request for patient " + pnumber
          }
        }
      } ~
      path(Segment / "specimens") { pnumber: String =>
        get {
          complete {
            "Received GET request for patient specimen totals " + pnumber
          }
        }
      } ~
      path(Segment / "specimens" / "counts") { pnumber: String =>
        get {
          complete {
            val counts = Set(
              SpecimenCount("plasma", 4),
              SpecimenCount("blood", 5))
            PatientSpecimenCounts(pnumber, counts)
          }
        }
      }
    }
  }
}

