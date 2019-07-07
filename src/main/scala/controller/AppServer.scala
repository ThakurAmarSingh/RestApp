package controller


import app.service.{AuthenticationService, JsonSupport, ResearchPaperService}
import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpEntity, StatusCodes}
import akka.stream.ActorMaterializer

import scala.io.StdIn
import conf.AppConf._
import akka.http.scaladsl.server.Directives
import models.Author
import spray.json._

object AppServer extends App with ResearchPaperService with Directives with JsonSupport with AuthenticationService {

    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher

    val route = authenticateBasic(realm = "secure site", myUserAuthenticator) { user =>
      path("get-all-research-papers") {
        get {
          complete(getAllResearchPapers.toJson)
        }
      } ~ path("get-research-papers") {
        get {
          parameter('title.as[String]) {
            title => complete(papersWithTitle(title).toJson)
          }
        } ~ get {
          parameter('id.as[Int]) {
            id => complete(researchPaperWithId(id).toJson)
          }
        } ~ get {
          parameter('author.as[String]) {
            author => complete(papersWithAuthor(author).toJson)
          }
        } ~ get {
          parameter('text.as[String]) {
            text => complete(papersWithContent(text).toJson)
          }
        }
      } ~ path("research-papers-between-dates") {
        get {
          parameter('from.as[String], 'until.as[String]) { (from, until) =>
            complete(papersWithInDates(from, until).toJson)
          }
        }
        //       ~ path("post-paper"){
        //        post{ entity(as[Author]){ author  =>
        //          println("**** " + author.name)
        //          complete("**** " + author.name)
        //          }
        //        }
        //      }
      }
    }
//ADD this to conf file
  val bindingFuture = akka.http.scaladsl.Http().bindAndHandle(route,localNodeAddress,localNodePort)

    println(s"Server online at http://$localNodeAddress:$localNodePort/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
}