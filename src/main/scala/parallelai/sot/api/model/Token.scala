package parallelai.sot.api.model

import io.circe.{Decoder, Encoder, HCursor, Json}
import spray.json.DefaultJsonProtocol._
import spray.json._
import org.apache.commons.lang3.SerializationUtils._
import parallelai.common.secure.{FromBytes, ToBytes}

case class Token(id: String, code: String, email: String)

object Token extends IdGenerator {
  implicit val tokenToBytes: ToBytes[Token] =
    (organisation: Token) => serialize(organisation)

  implicit val tokenFromBytes: FromBytes[Token] =
    (a: Array[Byte]) => deserialize[Token](a)

  implicit val rootJsonFormat: RootJsonFormat[Token] =
    jsonFormat3(Token.apply)

  implicit val tokenEncoder: Encoder[Token] = (a: Token) => Json.obj(
    "id" -> Json.fromString(a.id),
    "org_code" -> Json.fromString(a.code),
    "org_email" -> Json.fromString(a.email))

  implicit val tokenDecoder: Decoder[Token] = (c: HCursor) => for {
    code <- c.downField("org_code").as[String]
    email <- c.downField("org_email").as[String]
  } yield Token(uniqueId(), code, email)
}