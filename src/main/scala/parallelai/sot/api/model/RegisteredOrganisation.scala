package parallelai.sot.api.model

import io.circe.syntax._
import io.circe.{Decoder, Encoder, HCursor, Json}
import org.apache.commons.lang3.SerializationUtils.{deserialize, serialize}
import parallelai.common.secure.{Encrypted, FromBytes, ToBytes}

case class RegisteredOrganisation(orgSharedSecret: Encrypted[SharedSecret])

object RegisteredOrganisation {
  implicit val toBytes: ToBytes[RegisteredOrganisation] =
    (registeredOrganisation: RegisteredOrganisation) => serialize(registeredOrganisation.asJson)

  implicit val fromBytes: FromBytes[RegisteredOrganisation] =
    (a: Array[Byte]) => deserialize[Json](a).as[RegisteredOrganisation].right.get

  implicit val encoder: Encoder[RegisteredOrganisation] = (registeredOrganisation: RegisteredOrganisation) =>
    Json.obj("orgSharedSecret" -> registeredOrganisation.orgSharedSecret.asJson)

  implicit val decoder: Decoder[RegisteredOrganisation] = (c: HCursor) => for {
    orgSharedSecret <- c.downField("orgSharedSecret").as[Encrypted[SharedSecret]]
  } yield RegisteredOrganisation(orgSharedSecret)
}