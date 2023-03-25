package parallelai.sot.api.http

import java.nio.charset.StandardCharsets
import io.circe.parser._
import io.circe.syntax._
import io.circe.{Decoder, Encoder}
import com.softwaremill.sttp.{BodySerializer, ResponseAs, StringBody, asString}
import com.twitter.finagle.http.MediaType

trait ResultOps {
  implicit def result2String[T: Encoder]: Result[T] => String =
    _.asJson.noSpaces

  implicit def circeBodySerializer[T: Encoder]: BodySerializer[T] =
    t => StringBody(implicitly[Encoder[T]].apply(t).noSpaces, StandardCharsets.UTF_8.name, Some(MediaType.Json))

  // TODO - Clean up mess
  def asJson[T: Decoder]: ResponseAs[T, Nothing] =
    asString(StandardCharsets.UTF_8.name).map(s => parse(s).right.get.as[T].right.get)
}