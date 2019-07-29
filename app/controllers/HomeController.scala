package controllers

import java.io.File
import java.util.concurrent.Executors

import javax.inject._
import play.api.libs.Files
import play.api.mvc._
import services.GoogleVision

import services.JiraService
import java.time.LocalDate
import utils.Helper._
import services.EmailSender
 
/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with JiraService{
  val projectKey = "TES"
  val currentDay = LocalDate.now().getDayOfWeek.name()

  /**
    * Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def index = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def uploadFile: Action[MultipartFormData[Files.TemporaryFile]] = Action(parse.multipartFormData) { request =>
     var imageFileName = ""
    val localFilePath = request.body.file("fileUpload").map { image =>
       imageFileName = image.filename
      image.ref.moveFileTo(new File(".\\public\\images\\" + image.filename), true)
    }.get.toAbsolutePath
    val content = new File(localFilePath.toString).toPath
    val imageData = new GoogleVision(content).exec
   /* val updatedIssues = extractImageData(imageData).map{ userWeekdayMap  =>
      val Some((imageIssue, imageStatus)) = userWeekdayMap.get(currentDay)
      val jiraIssues = getProjectIssues(projectKey)
      val issue = jiraIssues.get.find(issue => issue.getKey.equalsIgnoreCase(imageIssue))
      val status = getIssueTransitions(issue.get.getKey)
      if(status.find(_.equalsIgnoreCase( imageStatus)).isDefined){
        changeStstus(issue.get,status.find(_.equalsIgnoreCase( imageStatus)).get)
      }else{
        //EmailSender.sendMessage("Status transition cannot executed",issue.get.getAssignee.getEmail)
      }
      (issue, status)
    }*/
   Ok(views.html.test(imageData.toString(),imageFileName))
  }

}
