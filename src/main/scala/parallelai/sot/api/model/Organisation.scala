package parallelai.sot.api.model

import io.circe.{Decoder, Encoder, HCursor, Json}
import spray.json.DefaultJsonProtocol._
import spray.json._
import org.apache.commons.lang3.SerializationUtils._
import parallelai.common.secure.{FromBytes, ToBytes}

case class Organisation(id: String, code: String, email: String)

object Organisation extends IdGenerator {
  implicit val organisationToBytes: ToBytes[Organisation] =
    (organisation: Organisation) => serialize(organisation)

  implicit val organisationFromBytes: FromBytes[Organisation] =
    (a: Array[Byte]) => deserialize[Organisation](a)

  implicit val rootJsonFormat: RootJsonFormat[Organisation] =
    jsonFormat3(Organisation.apply)

  implicit val encoder: Encoder[Organisation] = (a: Organisation) => Json.obj(
    "id" -> Json.fromString(a.id),
    "org_code" -> Json.fromString(a.code),
    "org_email" -> Json.fromString(a.email))

  implicit val organizationDecoder: Decoder[Organisation] = (c: HCursor) => for {
    code <- c.downField("org_code").as[String]
    email <- c.downField("org_email").as[String]
  } yield Organisation(uniqueId(), code, email)
}