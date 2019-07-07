package app.service
import models.ResearchPaper
import models.Author
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol{

    implicit val authorJsonWriter = new JsonWriter[Author] {
      def write(author:Author) : JsValue = JsObject("name" -> JsString(author.name))
    }

  implicit val listAuthorJsonWriter = new JsonWriter[List[Author]] {
    def write(list : List[Author]) : JsArray = JsArray(list.map(_.toJson).toVector)
  }

    implicit val researchPaperJsonWriter = new JsonWriter[ResearchPaper] {
     def write(researchPaper: ResearchPaper): JsValue = {
        JsObject(
          "id" -> JsNumber(researchPaper.id),
          "title" -> JsString(researchPaper.title),
          "authers" -> JsArray(researchPaper.authors.toJson),
          "publication" -> JsString(researchPaper.getDateAsString),
          "paperText" -> JsString(researchPaper.paperText)
        )
      }
    }

    implicit val listResearchPaperWriter = new JsonWriter[List[ResearchPaper]] {
      def write(list : List[ResearchPaper]) : JsArray = JsArray(list.map(_.toJson).toVector)
    }
}
