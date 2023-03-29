package parallelai.sot.api.model

import io.circe.{Decoder, Encoder, HCursor, Json}
import org.apache.commons.lang3.SerializationUtils._
import parallelai.common.secure.{FromBytes, ToBytes}

case class Token(id: String, code: String, email: String)

object Token {
  implicit val toBytes: ToBytes[Token] = serialize(_)

  implicit val fromBytes: FromBytes[Token] = deserialize[Token](_)

  implicit val encoder: Encoder[Token] = (t: Token) => Json.obj(
    "id" -> Json.fromString(t.id),
    "code" -> Json.fromString(t.code),
    "email" -> Json.fromString(t.email)
  )

  implicit val decoder: Decoder[Token] = (c: HCursor) => for {
    id <- c.downField("id").as[String]
    code <- c.downField("code").as[String]
    email <- c.downField("email").as[String]
  } yield Token(id, code, email)
}