package parallelai.sot.api.http

import io.circe.{Decoder, Encoder, HCursor, Json}

case class Errors(messages: String*)

object Errors {
  implicit val errorsEncoder: Encoder[Errors] = (errors: Errors) =>
    Json fromValues errors.messages.map(Json.fromString)

  // TODO - Clean up this mess
  implicit val errorsDecoder: Decoder[Errors] = (c: HCursor) =>
    Right(c.top.get.asArray.fold(Errors()) { jsonErrors =>
      Errors(jsonErrors.map(_.as[String].right.get): _*)
    })
}