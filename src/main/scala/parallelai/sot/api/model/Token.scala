package parallelai.sot.api.model

import java.util.Base64._
import javax.crypto.SecretKey

import io.circe.{Decoder, Encoder, HCursor, Json}
import org.apache.commons.lang3.SerializationUtils._
import parallelai.common.secure.{FromBytes, ToBytes}

case class Token(id: String, code: String, email: String, secret: Option[SecretKey])

object Token {
  implicit val toBytes: ToBytes[Token] = serialize(_)

  implicit val fromBytes: FromBytes[Token] = deserialize[Token](_)

  implicit val encoder: Encoder[Token] = (a: Token) => {
    val json = Json.obj(
      "id" -> Json.fromString(a.id),
      "code" -> Json.fromString(a.code),
      "email" -> Json.fromString(a.email)
    )

    a.secret.fold(json)(s => Json.obj("secret" -> Json.fromString(getEncoder encodeToString serialize(s))) deepMerge json)
  }

  implicit val decoder: Decoder[Token] = (c: HCursor) => for {
    id <- c.downField("id").as[String]
    code <- c.downField("code").as[String]
    email <- c.downField("email").as[String]
    secret <- c.downField("secret").as[Option[String]]
  } yield Token(id, code, email, secret.map(s => deserialize[SecretKey](getDecoder decode s)))
}