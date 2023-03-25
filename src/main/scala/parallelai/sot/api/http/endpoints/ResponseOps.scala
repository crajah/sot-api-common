package parallelai.sot.api.http.endpoints

import java.nio.charset.StandardCharsets
import spray.json._
import com.softwaremill.sttp.{BodySerializer, ResponseAs, StringBody, asString}
import com.twitter.finagle.http.MediaType

@deprecated(message = "Use parallelai.sot.api.http.ResultOps", since = "17th March 2018")
trait ResponseOps extends DefaultJsonProtocol {
  implicit val response2String: Response => String =
    _.toJson.prettyPrint

  implicit def jsonBodySerializer[T: JsonWriter]: BodySerializer[T] =
    t => StringBody(t.toJson.compactPrint, StandardCharsets.UTF_8.name, Some(MediaType.Json))

  def asJson[T: JsonReader]: ResponseAs[T, Nothing] =
    asString(StandardCharsets.UTF_8.name).map(_.parseJson.convertTo[T])
}