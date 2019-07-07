package app.service
import models.ResearchPaper

trait ResearchPaperService extends DataStoreService {
    def researchPaperWithId(id: Int): ResearchPaper = getResearchPaperWithId(id)
    def allResearchPapers: List[ResearchPaper] = getAllResearchPapers
    def papersWithTitle(str : String): List[ResearchPaper] = getResearchPaperWithTitle(str)
    def papersWithAuthor(str : String): List[ResearchPaper] = getResearchPaperWithAuthor(str)
    def papersWithInDates(from : String,to :String): List[ResearchPaper] = getResearchPaperWithInDates(from,to)
    def papersWithContent(str : String): List[ResearchPaper] = getResearchPaperWithText(str)
}
