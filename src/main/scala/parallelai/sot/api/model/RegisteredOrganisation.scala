package parallelai.sot.api.model

import java.util.Base64._
import io.circe.syntax._
import io.circe.{Decoder, Encoder, HCursor, Json}
import javax.crypto.SecretKey
import org.apache.commons.lang3.SerializationUtils.{deserialize, serialize}
import parallelai.common.secure.{FromBytes, ToBytes}

case class RegisteredOrganisation(code: String, secret: SecretKey)

object RegisteredOrganisation {
  implicit val toBytes: ToBytes[RegisteredOrganisation] =
    (registeredOrganisation: RegisteredOrganisation) => serialize(registeredOrganisation.asJson)

  implicit val fromBytes: FromBytes[RegisteredOrganisation] =
    (a: Array[Byte]) => deserialize[Json](a).as[RegisteredOrganisation].right.get

  implicit val encoder: Encoder[RegisteredOrganisation] = (registeredOrganisation: RegisteredOrganisation) =>
    Json.obj(
      "code" -> Json.fromString(registeredOrganisation.code),
      "secret" -> Json.fromString(getEncoder encodeToString serialize(registeredOrganisation.secret))
    )

  implicit val decoder: Decoder[RegisteredOrganisation] = (c: HCursor) => for {
    code <- c.downField("code").as[String]
    secret <- c.downField("secret").as[String]
  } yield RegisteredOrganisation(code, deserialize[SecretKey](getDecoder decode secret))
}