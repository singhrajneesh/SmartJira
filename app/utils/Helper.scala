package utils

import java.io.File
import java.nio.file.Path
import java.util.Base64
import org.apache.commons.io.FileUtils
import org.apache.log4j.Logger
import play.api.libs.Files
import play.api.mvc.MultipartFormData
import scala.util.{Failure, Success, Try}

object Helper {

  implicit class FunctionalTry[T](f: => T) {
    val logger: Logger = Logger.getLogger(getClass.getName)
    def check(customException: Throwable): T = Try(f) match {
      case Success(value) => value
      case Failure(ex) =>
        logger.info(customException.getMessage + ex.getMessage)
        throw ex
    }
  }

  implicit class Base64Conversion(file: File) {
    def convertToBASE64: String = {
      val base64 = FileUtils.readFileToByteArray(file)
      Base64.getEncoder.encodeToString(base64)
    }
  }

}
