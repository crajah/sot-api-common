package parallelai.sot.api.http.endpoints

import spray.json.DefaultJsonProtocol._
import spray.json._

// TODO - There is an io.finch.Error related to endpoint errors... could we use that instead?
@deprecated(message = "A new Error maybe created but currently using the new Errors", since = "17th March 2018")
case class Error(message: String)

object Error {
  implicit val rootJsonFormat: RootJsonFormat[Error] = jsonFormat(Error.apply, "error-message")
}