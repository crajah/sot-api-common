package parallelai.sot.api.http.endpoints

import spray.json.DefaultJsonProtocol._
import spray.json._

@deprecated(message = "Use parallelai.sot.api.http.Errors", since = "17th March 2018")
case class Errors(messages: String*)

object Errors {
  implicit val rootJsonFormat: RootJsonFormat[Errors] = jsonFormat(Errors.apply, "error-messages")
}