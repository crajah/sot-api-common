package parallelai.sot.api.model

import java.util.Base64._
import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder, HCursor, Json}
import spray.json._
import org.apache.commons.lang3.SerializationUtils.{deserialize, serialize}
import parallelai.common.secure.diffiehellman.ClientPublicKey
import parallelai.common.secure.{Encrypted, FromBytes, ToBytes}

case class Product(code: String, email: String, token: Encrypted, clientPublicKey: Option[ClientPublicKey] = None)

object Product extends DefaultJsonProtocol {
  implicit val productToBytes: ToBytes[Product] = serialize(_)

  implicit val productFromBytes: FromBytes[Product] = deserialize[Product](_)

  implicit val rootJsonFormat: RootJsonFormat[Product] =
    jsonFormat4(Product.apply)

  implicit val clientPublicKeyEncoder: Encoder[ClientPublicKey] = (clientPublicKey: ClientPublicKey) => Json.obj(
    ("value", Json.fromString(getEncoder encodeToString clientPublicKey.value))
  )

  implicit val clientPublicKeyDecoder: Decoder[ClientPublicKey] = (hCursor: HCursor) => for {
    valueString <- hCursor.downField("value").as[String]
  } yield ClientPublicKey(getDecoder decode valueString)

  implicit val encoder: Encoder[Product] = deriveEncoder

  implicit val decoder: Decoder[Product] = deriveDecoder
}

case class ProductToken(licenceId: String, code: String, email: String)

object ProductToken {
  implicit val tokenToBytes: ToBytes[ProductToken] = serialize(_)

  implicit val tokenFromBytes: FromBytes[ProductToken] = deserialize[ProductToken](_)
}