package parallelai.sot.api.http.endpoints

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import spray.json._
import spray.json.lenses.JsonLenses._
import com.twitter.finagle.http.Status
import parallelai.sot.api.json.JsonLens._

@deprecated(message = "Use parallelai.sot.api.http.Result", since = "17th March 2018")
case class Response(content: JsValue, status: Status)

// TODO - Hateoas, such as links
object Response extends DefaultJsonProtocol {
  implicit val statusEncoder: Encoder[Status] = deriveEncoder

  implicit val statusDecoder: Decoder[Status] = deriveDecoder

  implicit val statusJsonWriter: JsonWriter[Status] =
    (status: Status) => JsObject(
      "code" -> JsNumber(status.code),
      "reason" -> JsString(status.reason))

  implicit val statusJsonReader: JsonReader[Status] =
    (json: JsValue) => Status(json.extract[Int]("code"))

  implicit val responseJsonWriter: JsonWriter[Response] =
    (response: Response) => JsObject("content" -> response.content) << ("http-status", response.status.toJson)

  implicit val responseJsonReader: JsonReader[Response] =
    (json: JsValue) => Response(json.extract[JsValue]("content"), json.extract[Status]("http-status"))

  def apply[J: JsonWriter](j: J, status: Status = Status.Ok) =
    new Response(implicitly[JsonWriter[J]].write(j), status)

  // TODO - There is an io.finch.Error related to endpoint errors... could we use that instead?
  @deprecated(message = "A new Error maybe created but currently using the new Errors", since = "17th March 2018")
  case class Error(message: String)

  object Error {
    implicit val rootJsonFormat: RootJsonFormat[Error] = jsonFormat(Error.apply, "error-message")
  }

  @deprecated(message = "Use parallelai.sot.api.http.Errors", since = "17th March 2018")
  case class Errors(messages: String*)

  object Errors {
    implicit val rootJsonFormat: RootJsonFormat[Errors] = jsonFormat(Errors.apply, "error-messages")
  }
}