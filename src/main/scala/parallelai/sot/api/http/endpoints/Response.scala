package parallelai.sot.api.http.endpoints

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import spray.json._
import com.twitter.finagle.http.Status
import parallelai.sot.api.json.JsonLens._
import spray.json.lenses.JsonLenses._

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
}