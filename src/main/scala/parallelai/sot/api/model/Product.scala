package parallelai.sot.api.model

import java.util.Base64._
import io.circe.generic.semiauto._
import io.circe.syntax._
import io.circe.{Decoder, Encoder, HCursor, Json}
import org.apache.commons.lang3.SerializationUtils.{deserialize, serialize}
import parallelai.common.secure.diffiehellman.ClientPublicKey
import parallelai.common.secure.{Encrypted, FromBytes, ToBytes}

case class Product(code: String, email: String, token: Encrypted[ProductToken], clientPublicKey: Option[ClientPublicKey] = None)

object Product {
  implicit val toBytes: ToBytes[Product] =
    (product: Product) => serialize(product.asJson)

  implicit val fromBytes: FromBytes[Product] =
    (a: Array[Byte]) => deserialize[Json](a).as[Product].right.get

  // TODO
  // implicit val rootJsonFormat: RootJsonFormat[Product] = jsonFormat4(Product.apply)

  implicit val clientPublicKeyEncoder: Encoder[ClientPublicKey] = (clientPublicKey: ClientPublicKey) => Json.obj(
    ("value", Json.fromString(getEncoder encodeToString clientPublicKey.value))
  )

  implicit val clientPublicKeyDecoder: Decoder[ClientPublicKey] = (hCursor: HCursor) => for {
    valueString <- hCursor.downField("value").as[String]
  } yield ClientPublicKey(getDecoder decode valueString)

  implicit val encoder: Encoder[Product] = (product: Product) => {
    val json = Json.obj(
      "code" -> Json.fromString(product.code),
      "email" -> Json.fromString(product.email),
      "token" -> product.token.asJson
    )

    val clientPublicKey: Option[Json] =
      product.clientPublicKey.map(c => Json.obj("clientPublicKey" -> c.asJson))

    clientPublicKey.fold(json) { _ deepMerge json }
  }

  implicit val decoder: Decoder[Product] = (c: HCursor) => for {
    code <- c.downField("code").as[String]
    email <- c.downField("email").as[String]
    token <- c.downField("token").as[Encrypted[ProductToken]]
    clientPublicKey <- c.downField("clientPublicKey").as[Option[ClientPublicKey]]
  } yield Product(code, email, token, clientPublicKey)
}

case class ProductToken(licenceId: String, code: String, email: String)

object ProductToken {
  implicit val toBytes: ToBytes[ProductToken] = serialize(_)

  implicit val fromBytes: FromBytes[ProductToken] = deserialize[ProductToken](_)

  implicit val encoder: Encoder[ProductToken] = deriveEncoder

  implicit val decoder: Decoder[ProductToken] = deriveDecoder
}