package services

import java.net.URL

import javax.mail.{PasswordAuthentication, Session}
import net.rcarz.jiraclient.JiraClient
import net.rcarz.jiraclient.BasicCredentials

trait GoogleAccess extends GoogleCredentials {
  final val getServerURl = new URL(targetURL + apiKey)
}

trait GmailAccess extends GmailCredentials{
  final val session = Session.getInstance(props, new javax.mail.Authenticator(){  new PasswordAuthentication(emailID, password)})
}

trait JiraAccess extends JiraCredentials {
  final val basicCredentials: BasicCredentials = new BasicCredentials(userName, password)
  final val jiraClient = new JiraClient(uri, basicCredentials)
}

