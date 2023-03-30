package parallelai.sot.api.model

import java.util.Base64._
import io.circe.syntax._
import io.circe.{Decoder, Encoder, HCursor, Json}
import org.apache.commons.lang3.SerializationUtils.{deserialize, serialize}
import parallelai.common.secure.diffiehellman.{ClientPublicKey, ServerPublicKey}
import parallelai.common.secure.{Encrypted, FromBytes, ToBytes}

case class Product(code: String, email: String, token: Option[Encrypted[Token]] = None, clientPublicKey: Option[ClientPublicKey] = None)

object Product {
  implicit val toBytes: ToBytes[Product] =
    (product: Product) => serialize(product.asJson)

  implicit val fromBytes: FromBytes[Product] =
    (a: Array[Byte]) => deserialize[Json](a).as[Product].right.get

  implicit val clientPublicKeyEncoder: Encoder[ClientPublicKey] = (clientPublicKey: ClientPublicKey) => Json.obj(
    ("value", Json.fromString(getEncoder encodeToString clientPublicKey.value))
  )

  implicit val clientPublicKeyDecoder: Decoder[ClientPublicKey] = (hCursor: HCursor) => for {
    valueString <- hCursor.downField("value").as[String]
  } yield ClientPublicKey(getDecoder decode valueString)

  implicit val encoder: Encoder[Product] = (product: Product) => {
    val json = Json.obj(
      "code" -> Json.fromString(product.code),
      "email" -> Json.fromString(product.email)
    )

    val token: Option[Json] =
      product.token.map(c => Json.obj("token" -> c.asJson))

    val clientPublicKey: Option[Json] =
      product.clientPublicKey.map(c => Json.obj("clientPublicKey" -> c.asJson))

    Seq(token, clientPublicKey).flatten.foldLeft(json) { (acc, json) => json deepMerge acc }
  }

  implicit val decoder: Decoder[Product] = (c: HCursor) => for {
    code <- c.downField("code").as[String]
    email <- c.downField("email").as[String]
    token <- c.downField("token").as[Option[Encrypted[Token]]]
    clientPublicKey <- c.downField("clientPublicKey").as[Option[ClientPublicKey]]
  } yield Product(code, email, token, clientPublicKey)
}

case class RegisteredProduct(serverPublicKey: ServerPublicKey, apiSharedSecret: Encrypted[SharedSecret])

object RegisteredProduct {
  implicit val toBytes: ToBytes[RegisteredProduct] =
    (registeredProduct: RegisteredProduct) => serialize(registeredProduct.asJson)

  implicit val fromBytes: FromBytes[RegisteredProduct] =
    (a: Array[Byte]) => deserialize[Json](a).as[RegisteredProduct].right.get

  implicit val encoder: Encoder[RegisteredProduct] = (registeredProduct: RegisteredProduct) =>
    Json.obj(
      "serverPublicKey" -> registeredProduct.serverPublicKey.asJson,
      "apiSharedSecret" -> registeredProduct.apiSharedSecret.asJson
    )

  implicit val decoder: Decoder[RegisteredProduct] = (c: HCursor) => for {
    serverPublicKey <- c.downField("serverPublicKey").as[ServerPublicKey]
    apiSharedSecret <- c.downField("apiSharedSecret").as[Encrypted[SharedSecret]]
  } yield RegisteredProduct(serverPublicKey, apiSharedSecret)
}