package services

import net.rcarz.jiraclient.{Field, Issue}
import net.sf.json.{JSONArray, JSONObject}

import scala.collection.mutable
import scala.util.Try
import scala.util.matching.Regex
import scala.util._

trait JiraService extends JiraAccess {

  def projectKey: String

  private val weekdays = List("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY")

  def extractImageData(message: String): List[Map[String, (String, String)]] = {
    val namesRegex = raw"""([A-Z])\w+(?=\s$projectKey)""".r
    namesRegex.findAllIn(message).toList.map { name =>
      val statusWithNameRegex = raw"""$name(\s([\w]+.[0-9]+)\s\([\w ]+\))*""".r
      val statusWithName = statusWithNameRegex.findFirstIn(message).get
      val statusRegex = raw"""(\s([\w]+.[0-9]+)\s\([\w ]+\))""".r
      val pattern = new Regex("""([\w]+-[0-9]+) (\([\w ]+\))""", "story", "status")
      val tupleList = statusRegex.findAllIn(statusWithName).map { x =>
        var t = x.replaceAll("\n", " ").trim()
        val matches = pattern.findFirstMatchIn(t).get
        (matches.group("story"), matches.group("status").replaceAll("[^a-zA-Z ]+", ""))
      }.toList
      weekdays.zip(tupleList).toMap.mapValues { case (story, issue) =>
        if (issue.equalsIgnoreCase("In Progress"))
          (story, "Start Progress")
        else
          (story, issue)
      }
    }
  }

  def getProjectIssues(projectKey: String): Option[mutable.Buffer[Issue]] = {
    import scala.collection.JavaConverters._
    Try(jiraClient.searchIssues(s"project=$projectKey").issues.asScala).toOption
  }

  def getIssue(id: String): Option[Issue] = {
    Try(jiraClient.getIssue(id)).toOption
  }

  def addComment(issue: Issue, comment: String): Unit = {
    issue.addComment(comment)
  }

  def changeStstus(issue: Issue, status: String): Option[Issue] = {
    Try{issue.transition().execute(status)} match {
      case Success(value) => 
        EmailSender.sendMessage(s"Status transition changed to ${issue.getDescription}",issue.getAssignee.getEmail)
        getIssue(issue.getKey)
      case Failure(ex) => 
       // EmailSender.sendMessage("Status transition cannot executed",issue.getAssignee.getEmail)
      None
    }
    
  }

  def createIssue(projectKey: String, summary: String): Issue = {
    jiraClient
      .createIssue(projectKey, "Task")
      .field(Field.SUMMARY, summary)
      .execute()
  }

  lazy val getStatusList: String => Map[String, List[String]] = { projectKey => {
    val jsonArray = JSONArray.fromObject(jiraClient.getRestClient.get(s"/rest/api/2/project/$projectKey/statuses"))
    for (i <- 0 until jsonArray.size()) yield {
      val obj = jsonArray.getJSONObject(i)
      val `type` = obj.getString("name")
      val statusList = for (j <- 0 until obj.getJSONArray("statuses").size) yield {
        obj.getJSONArray("statuses").getJSONObject(j).getString("name")
      }
      `type` -> statusList.toList
    }
  }.toMap
  }

  def getIssueTransitions(issueId: String): List[String] = {
    val transitionsArray = JSONObject.fromObject(jiraClient.getRestClient.get(s"/rest/api/2/issue/$issueId/transitions")).getJSONArray("transitions")
    val transitions = for (i <- 0 until transitionsArray.size()) yield {
      transitionsArray.getJSONObject(i).getString("name")
    }
    transitions.toList
  }

}
