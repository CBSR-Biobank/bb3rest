package org.biobank.rest

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._
import org.json4s.DefaultFormats

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


// this trait defines our service behavior independently from the service actor
trait PatientService extends HttpService {

  val json4sFormats = DefaultFormats
  val patientRoute =
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
            "Received GET request for patient specimen counts " + pnumber
          }
        }
      }
    }
}

