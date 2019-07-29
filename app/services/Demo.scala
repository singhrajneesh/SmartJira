package services

import java.time.LocalDate

object Demo extends App with JiraService {
  val projectKey = "TES"
  private val name = "Arun"
  //(?<=\()[\w ]+(?=\))
  val currentDay = LocalDate.now().getDayOfWeek.name()
  //val message = "Monday Tuesday Wednesday Thursday Friday Arun TES-1 (In Progress) TES-2 (In Progress) TES-3 (In Progress) TES-6 (Done) TES-7 (Done) Kanwar TES-4 (In Progress) TES-2 (Closed) TES-3 (Closed) TES-1 (QA) TES-4 (QA) "
  
  val message = "MONDAY TUESDAY WEDNESDAY THURSDAY FRIDAY ARJUN TES-4 (IN PROGRESS) TES-3 (DONE) TES-6 (IN REVIEW) TES-7 (DONE) TES-4 (DONE) KARAN TES-1 (IN PROGRESS) TES-5 (DONE) TES-1 (DONE) TES-6 (IN REVIEW) TES-2 (DONE)"
  val updatedIssues = extractImageData(message).map{ userWeekdayMap  =>
    val Some((imageIssue, imageStatus)) = userWeekdayMap.get(currentDay)
    val issueStatusTuple = for {
      jiraIssues <- getProjectIssues(projectKey)
      issue <- jiraIssues.find(issue => issue.getKey == imageIssue)
      status <- getIssueTransitions(issue.getKey).find(_ == imageStatus)
    } yield {
      (issue, status)
    }
    issueStatusTuple match {
      case Some((issue,status)) => changeStstus(issue,status)
      case None => None
    }
  }

  println(updatedIssues.toString())

}