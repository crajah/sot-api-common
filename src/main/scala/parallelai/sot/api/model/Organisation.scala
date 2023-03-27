package parallelai.sot.api.model

import io.circe.syntax._
import io.circe.{Decoder, Encoder, HCursor, Json}
import org.apache.commons.lang3.SerializationUtils._
import parallelai.common.secure._

case class Organisation(code: String, email: String, token: Option[Encrypted[Token]])

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

    token.fold(json) { _ deepMerge json }
  }

  implicit val decoder: Decoder[Organisation] = (c: HCursor) => for {
    code <- c.downField("code").as[String]
    email <- c.downField("email").as[String]
    token <- c.downField("token").as[Option[Encrypted[Token]]]
  } yield Organisation(code, email, token)
}