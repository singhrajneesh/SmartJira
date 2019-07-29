package exceptions

case class PutObjectException(message: String) extends Exception(message: String)
case class PreSignedURLException(message: String) extends Exception(message: String)
case class InvalidFile(message: String) extends Exception(message: String)
case class BucketFetchException(message: String) extends Exception(message: String)
case class GoogleConnectionException(message: String) extends Exception(message: String)
case class ImageParsingException(message: String) extends Exception(message: String)
case class SendingEmailException(message: String)extends Exception(message:String)