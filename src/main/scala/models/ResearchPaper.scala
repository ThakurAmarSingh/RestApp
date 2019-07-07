package models

import java.text.SimpleDateFormat
import java.util.Date

case class Author(name :String)

case class ResearchPaper(id : Int, //TODO Needs to be unique
                         title : String,
                         authors : List[Author],
                         publicationDate : Date,
                         paperText : String) {
  val DATE_FORMAT = "dd/MMM/yyyy"
  implicit def getDateAsString: String =  new SimpleDateFormat(DATE_FORMAT).format(publicationDate)
  def StringToDate(s : String): Date =  new SimpleDateFormat(DATE_FORMAT).parse(s)
}


