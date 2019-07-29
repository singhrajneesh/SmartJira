package services

import java.io.{BufferedWriter, FileInputStream, OutputStreamWriter}
import java.nio.file.Path
import java.util

import com.google.cloud.vision.v1.Feature.Type
import com.google.cloud.vision.v1._
import com.google.protobuf.ByteString

import scala.collection.JavaConverters._

class GoogleVision(content:Path) extends GoogleAccess {

  import utils.Helper._
  def exec: String = {
    val requests = new util.ArrayList[AnnotateImageRequest]

    val imgBytes: ByteString = ByteString.readFrom(new FileInputStream(content.toString))

    val img: Image = Image.newBuilder().setContent(imgBytes).build()
    val feat: Feature = Feature.newBuilder().setType(Type.DOCUMENT_TEXT_DETECTION).build()
    val request: AnnotateImageRequest = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build()
    requests.add(request)

    val client: ImageAnnotatorClient = ImageAnnotatorClient.create()
    val response: BatchAnnotateImagesResponse = client.batchAnnotateImages(requests)
    val responses = response.getResponsesList.asScala.toList
    client.close()
    //EmailSender.sendMessage(responses(0).getTextAnnotations(0).getDescription)
    responses(0).getTextAnnotations(0).getDescription
  }


}
