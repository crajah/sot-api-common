package parallelai.sot.api.model

import io.circe.{Decoder, Encoder, HCursor, Json}
import io.circe.syntax._
import parallelai.common.secure._
import org.apache.commons.lang3.SerializationUtils._

case class Organisation(code: String, email: String, token: Encrypted[Token])

object Organisation extends IdGenerator {

  implicit val organisationToBytes: ToBytes[Organisation] =
    (organisationTwo: Organisation) => serialize(organisationTwo)

  implicit val organisationTwoFromBytes: FromBytes[Organisation] =
    (a: Array[Byte]) => deserialize[Organisation](a)

  implicit val encoder: Encoder[Organisation] = (a: Organisation) => Json.obj(
    "token" -> a.token.asJson,
    "code" -> Json.fromString(a.code),
    "email" -> Json.fromString(a.email))


  implicit val organizationDecoder: Decoder[Organisation] = (c: HCursor) => for {
    token <- c.downField("token").as[Encrypted[Token]]
    code <- c.downField("code").as[String]
    email <- c.downField("email").as[String]
  } yield Organisation(code, email, token)
}
