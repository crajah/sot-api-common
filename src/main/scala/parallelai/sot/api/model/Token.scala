package parallelai.sot.api.model

import java.util.Base64._
import io.circe.{Decoder, Encoder, HCursor, Json}
import javax.crypto.SecretKey
import org.apache.commons.lang3.SerializationUtils._
import parallelai.common.secure.{FromBytes, ToBytes}

case class Token(id: String, code: String, email: String, secret: Option[SecretKey] = None)

object Token {
  implicit val toBytes: ToBytes[Token] = serialize(_)

  implicit val fromBytes: FromBytes[Token] = deserialize[Token](_)

  implicit val encoder: Encoder[Token] = (t: Token) => {
    val json = Json.obj(
      "id" -> Json.fromString(t.id),
      "code" -> Json.fromString(t.code),
      "email" -> Json.fromString(t.email)
    )

    t.secret.fold(json) { secret =>
      Json.obj("secret" -> Json.fromString(getEncoder encodeToString serialize(secret))) deepMerge json
    }
  }

  implicit val decoder: Decoder[Token] = (c: HCursor) => for {
    id <- c.downField("id").as[String]
    code <- c.downField("code").as[String]
    email <- c.downField("email").as[String]
    secret <- c.downField("secret").as[Option[String]]
  } yield Token(id, code, email, secret.map(s => deserialize[SecretKey](getDecoder decode s)))
}