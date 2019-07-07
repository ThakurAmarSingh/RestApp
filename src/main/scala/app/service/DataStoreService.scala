package app.service
import java.text.SimpleDateFormat
import java.util.Date

import models._

trait DataStoreService {

  val DATE_FORMAT = "dd/MMM/yyyy"

  implicit def StringToDate(s : String): Date =  new SimpleDateFormat(DATE_FORMAT).parse(s)

  def getAllResearchPapers : List[ResearchPaper] = {
    val bufferedSource = io.Source.fromFile("./files/primaryLog.csv")
    val rs = bufferedSource.getLines
    stringToResearchPaper(rs.toList)
  }

  def getResearchPaperWithId(id : Int) : ResearchPaper = {
    val bufferedSource = io.Source.fromFile("./files/primaryLog.csv")
      val rs = bufferedSource.getLines.filter(findPaperWithText(id.toString,0,_))
    stringToResearchPaperWithFullText(rs.toList)
  }

  def getResearchPaperWithTitle(str : String) : List[ResearchPaper] = {
    val bufferedSource = io.Source.fromFile("./files/primaryLog.csv")
    val rs = bufferedSource.getLines.filter(findPaperWithText(str,1,_))
    stringToResearchPaper(rs.toList)
  }

  def getResearchPaperWithAuthor(str : String) : List[ResearchPaper] = {
    val bufferedSource = io.Source.fromFile("./files/primaryLog.csv")
    val rs = bufferedSource.getLines.filter(findPaperWithText(str,2,_))
    stringToResearchPaper(rs.toList)
  }

  def getResearchPaperWithInDates(from : String,to: String) : List[ResearchPaper] = {

    val bufferedSource = io.Source.fromFile("./files/primaryLog.csv")
    val rs = bufferedSource.getLines
    stringToResearchPaper(rs.toList).filter(p => p.publicationDate.after(from) && p.publicationDate.before(to))
  }

  def getResearchPaperWithText(str : String) : List[ResearchPaper] = {
    val bufferedSource = io.Source.fromFile("./files/primaryLog.csv")
    val rs = bufferedSource.getLines
    stringToResearchPaperFileName(rs.toList,str).flatten
  }

  private def findPaperWithText(str : String,pos : Int,line : String) = {
    val ar = line.split(",")
    ar(pos).toLowerCase.contains(str.toLowerCase)
  }

  def stringToResearchPaper(lines : List[String]) : List[ResearchPaper] = {
      lines.map(str => str.split(",") match {
        case Array(a,b,c,d,e) => ResearchPaper(a.toString.toInt,b.toString,strToAuthors(c),d,papersTextWithLimit(e.toString))
      })
  }

  def stringToResearchPaperWithFullText(lines : List[String]) : ResearchPaper = {
    lines.head.split(",") match {
      case Array(a,b,c,d,e) => ResearchPaper(a.toString.toInt,b.toString,strToAuthors(c),d,papersTextWithOutLimit(e.toString))
    }
  }

  def stringToResearchPaperFileName(lines : List[String],text: String) : List[Option[ResearchPaper]] = {
    lines.map(str => str.split(",") match {
      case Array(a,b,c,d,e) =>
        if (io.Source.fromFile(s"./files/${e}").getLines.mkString("\n").toLowerCase.contains(text.toLowerCase)) {
          Some(ResearchPaper(a.toString.toInt, b.toString, strToAuthors(c), StringToDate(d.toString), papersTextWithLimit(e.toString)))
        } else None
    })
  }

  def strToAuthors(str: String) : List[Author] = str.split(":").map(Author(_)).toList

  def papersTextWithLimit(fileName : String): String = {
    val bufferedSource = io.Source.fromFile(s"./files/$fileName")
    val (rs,_) = bufferedSource.getLines.toList.splitAt(20)
    rs.mkString("\n")
  }

  def papersTextWithOutLimit(fileName : String): String = {
    val bufferedSource = io.Source.fromFile(s"./files/$fileName")
     bufferedSource.getLines.toList.mkString("\n")
  }
}
