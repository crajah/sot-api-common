package parallelai.sot.api.model

import java.util.Base64._
import io.circe.syntax._
import io.circe.{Decoder, Encoder, HCursor, Json}
import javax.crypto.SecretKey
import org.apache.commons.lang3.SerializationUtils.{deserialize, serialize}
import parallelai.common.secure.{FromBytes, ToBytes}

case class ApiSharedSecret(licenceId: String, apiServerSecret: SecretKey)

object ApiSharedSecret {
  implicit val toBytes: ToBytes[ApiSharedSecret] =
    (apiSharedSecret: ApiSharedSecret) => serialize(apiSharedSecret.asJson)

  implicit val fromBytes: FromBytes[ApiSharedSecret] =
    (a: Array[Byte]) => deserialize[Json](a).as[ApiSharedSecret].right.get

  implicit val encoder: Encoder[ApiSharedSecret] = (apiSharedSecret: ApiSharedSecret) =>
    Json.obj(
      "licenceId" -> Json.fromString(apiSharedSecret.licenceId),
      "apiServerSecret" -> Json.fromString(getEncoder encodeToString serialize(apiSharedSecret.apiServerSecret))
    )

  implicit val decoder: Decoder[ApiSharedSecret] = (c: HCursor) => for {
    licenceId <- c.downField("licenceId").as[String]
    apiServerSecret <- c.downField("apiServerSecret").as[String]
  } yield ApiSharedSecret(licenceId, deserialize[SecretKey](getDecoder decode apiServerSecret))
}