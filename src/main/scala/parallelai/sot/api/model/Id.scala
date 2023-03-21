package parallelai.sot.api.model

import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat

case class Id(value: String) extends AnyVal

object Id {
  implicit val rootJsonFormat: RootJsonFormat[Id] = jsonFormat(Id.apply, "id")
}