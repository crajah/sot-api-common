package parallelai.sot.api.model

import java.util.Base64._
import io.circe.syntax._
import io.circe.{Decoder, Encoder, HCursor, Json}
import javax.crypto.SecretKey
import org.apache.commons.lang3.SerializationUtils.{deserialize, serialize}
import parallelai.common.secure.{FromBytes, ToBytes}

case class SharedSecret(id: String, secret: SecretKey)

object SharedSecret {
  implicit val toBytes: ToBytes[SharedSecret] =
    (sharedSecret: SharedSecret) => serialize(sharedSecret.asJson)

  implicit val fromBytes: FromBytes[SharedSecret] =
    (a: Array[Byte]) => deserialize[Json](a).as[SharedSecret].right.get

  implicit val encoder: Encoder[SharedSecret] = (sharedSecret: SharedSecret) =>
    Json.obj(
      "id" -> Json.fromString(sharedSecret.id),
      "secret" -> Json.fromString(getEncoder encodeToString serialize(sharedSecret.secret))
    )

  implicit val decoder: Decoder[SharedSecret] = (c: HCursor) => for {
    id <- c.downField("id").as[String]
    secret <- c.downField("secret").as[String]
  } yield SharedSecret(id, deserialize[SecretKey](getDecoder decode secret))
}