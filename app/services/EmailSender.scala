package services

import exceptions.SendingEmailException
import javax.mail._
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

object EmailSender extends GmailAccess {
  import utils.Helper._

  def sendMessage(response: String,userEmail:String): Unit = {
    val EmailList=new Array[Address](1)
    EmailList(0)=new InternetAddress(userEmail)

    val session = Session.getInstance(props, new Authenticator() {
      override protected def getPasswordAuthentication = new PasswordAuthentication(emailID, password)
    })

    val message = new MimeMessage(session)
    message.setFrom(new InternetAddress("singhrajneesh065@gmail.com"))
    message.setRecipients(Message.RecipientType.TO, EmailList)
    val responseInList=response.split("\n").toList
    message.setSubject("Smart Jira Notification")
    message.setText(response)
    Transport.send(message)
    System.out.println("Done")
  }.check(SendingEmailException("Email Sending mail"))
}
