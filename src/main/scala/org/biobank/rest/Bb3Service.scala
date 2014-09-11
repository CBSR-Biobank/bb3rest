package org.biobank.rest

import org.biobank.rest.domain._

import akka.actor.{ Actor, Props }
import spray.routing._
import spray.http._
import MediaTypes._
import spray.json.DefaultJsonProtocol

/**
  * we don't implement our route structure directly in the service actor because
  * we want to be able to test it independently, without having to spin up an actor
  */
class Bb3ServiceActor extends Actor with Bb3Service {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(patientRoute)
}

/** This trait defines our service behavior independently from the service actor.
  *
  * Example taken from here:
  *
  * http://gagnechris.wordpress.com/2013/09/15/building-restful-apis-with-scala-using-spray/
  */
trait Bb3Service extends HttpService {

  val patientRoute = {
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
      path(Segment / "specimens" / "counts") { pnumber =>
        requestContext =>
        val patientService = actorRefFactory.actorOf(Props(new PatientService(requestContext)))
        patientService ! PatientService.SpecimenCounts(pnumber)
      }
    }
  }
}

