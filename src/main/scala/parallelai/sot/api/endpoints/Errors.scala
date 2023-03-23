package parallelai.sot.api.endpoints

import spray.json.DefaultJsonProtocol._
import spray.json._

case class Errors(messages: String*)

object Errors {
  implicit val rootJsonFormat: RootJsonFormat[Errors] = jsonFormat(Errors.apply, "error-messages")
}