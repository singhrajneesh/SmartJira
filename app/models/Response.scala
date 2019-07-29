package models

case class Response(responses: List[Annotations])

case class Annotations(
  textAnnotations: List[Any],
  fullTextAnnotation: FullTextAnnotation
)

case class FullTextAnnotation(pages: List[Data])

case class Data(
  obj: Any,
  text: String
)
