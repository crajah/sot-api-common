package parallelai.sot.api.model

import java.util.Base64.{getDecoder, getEncoder}
import io.circe.syntax._
import io.circe.{Decoder, Encoder, HCursor, Json}
import javax.crypto.SecretKey
import org.apache.commons.lang3.SerializationUtils.{deserialize, serialize}
import parallelai.common.secure.{Encrypted, FromBytes, ToBytes}

case class Organisation(code: String, email: String, token: Option[Encrypted[Token]], secret: Option[SecretKey])

object Organisation {
  implicit val toBytes: ToBytes[Organisation] =
    (organisation: Organisation) => serialize(organisation.asJson)

  implicit val fromBytes: FromBytes[Organisation] =
    (a: Array[Byte]) => deserialize[Json](a).as[Organisation].right.get

  implicit val encoder: Encoder[Organisation] = (organisation: Organisation) => {
    val json = Json.obj(
      "code" -> Json.fromString(organisation.code),
      "email" -> Json.fromString(organisation.email)
    )

    val token: Option[Json] =
      organisation.token.map(c => Json.obj("token" -> c.asJson))

    val secret: Option[Json] =
      organisation.secret.map(secret => Json.obj("secret" -> Json.fromString(getEncoder encodeToString serialize(secret))))

    Seq(token, secret).flatten.foldLeft(json) { (acc, json) => json deepMerge acc }
  }

  implicit val decoder: Decoder[Organisation] = (c: HCursor) => for {
    code <- c.downField("code").as[String]
    email <- c.downField("email").as[String]
    token <- c.downField("token").as[Option[Encrypted[Token]]]
    secret <- c.downField("secret").as[Option[String]]
  } yield Organisation(code, email, token, secret.map(s => deserialize[SecretKey](getDecoder decode s)))
}

case class LicencedOrganisation(id: String, organisation: Organisation, token: Encrypted[Token])

object LicencedOrganisation {
  implicit val toBytes: ToBytes[LicencedOrganisation] =
    (licencedOrganisation: LicencedOrganisation) => serialize(licencedOrganisation.asJson)

  implicit val fromBytes: FromBytes[LicencedOrganisation] =
    (a: Array[Byte]) => deserialize[Json](a).as[LicencedOrganisation].right.get

  implicit val encoder: Encoder[LicencedOrganisation] = (licencedOrganisation: LicencedOrganisation) =>
    Json.obj(
      "id" -> Json.fromString(licencedOrganisation.id),
      "organisation" -> licencedOrganisation.organisation.asJson,
      "token" -> licencedOrganisation.token.asJson
    )

  implicit val decoder: Decoder[LicencedOrganisation] = (c: HCursor) => for {
    id <- c.downField("id").as[String]
    organisation <- c.downField("organisation").as[Organisation]
    token <- c.downField("token").as[Encrypted[Token]]
  } yield LicencedOrganisation(id, organisation, token)
}

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

case class OrganisationSharedSecret(licenceId: String, apiServerSecret: SecretKey)