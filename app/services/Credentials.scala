package services

import java.util.Properties

import com.typesafe.config.{Config, ConfigFactory}
import org.apache.log4j.Logger

import scala.util.Try

object ValidationImplicits {
  val logger = Logger.getLogger(getClass.getName)

  implicit class Check(conf: Config) {
    def validatedString(path: String): String = {
      Try(conf.getString(path)).toOption.fold {
        logger.error(s"Cannot find property for '$path' ")
        ""
      }(identity)
    }
  }


  def SmtpProperties: Properties = {
    val props = new Properties
    props.put("mail.smtp.auth", "true")
    props.put("mail.smtp.starttls.enable", "true")
    props.put("mail.smtp.host", "smtp.gmail.com")
    props.put("mail.smtp.port", "587")
    props
  }


}

trait GoogleCredentials{
  import ValidationImplicits._
  val defaultConfig: Config = ConfigFactory.load()
  final val targetURL: String = defaultConfig.validatedString("google.targetURL")
  final val apiKey: String = defaultConfig.validatedString("google.apiKey")
}

trait GmailCredentials{
  import ValidationImplicits._
  val defaultConfig: Config = ConfigFactory.load()
  final val emailID:String=defaultConfig.validatedString("gmail.emailId")
  final val password:String=defaultConfig.validatedString("gmail.password")
  final val props:Properties=SmtpProperties
}

trait JiraCredentials{
  import ValidationImplicits._
  val defaultConfig: Config = ConfigFactory.load()

  val uri: String = defaultConfig.validatedString("jira.uri")
  val userName: String = defaultConfig.validatedString("jira.userName")
  val password: String = defaultConfig.validatedString("jira.password")

}
