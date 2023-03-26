package parallelai.sot.api.model

import io.circe.syntax._
import io.circe.{Decoder, Encoder, HCursor, Json}
import org.apache.commons.lang3.SerializationUtils.{deserialize, serialize}
import parallelai.common.secure.diffiehellman.ServerPublicKey
import parallelai.common.secure.{Encrypted, FromBytes, ToBytes}

case class RegisteredProduct(serverPublicKey: ServerPublicKey, apiSharedSecret: Encrypted[ApiSharedSecret])

object RegisteredProduct {
  implicit val encoder: Encoder[RegisteredProduct] = (registeredProduct: RegisteredProduct) =>
    Json.obj(
      "serverPublicKey" -> registeredProduct.serverPublicKey.asJson,
      "apiSharedSecret" -> registeredProduct.apiSharedSecret.asJson
    )

  implicit val decoder: Decoder[RegisteredProduct] = (c: HCursor) => for {
    serverPublicKey <- c.downField("serverPublicKey").as[ServerPublicKey]
    apiSharedSecret <- c.downField("apiSharedSecret").as[Encrypted[ApiSharedSecret]]
  } yield RegisteredProduct(serverPublicKey, apiSharedSecret)

  implicit val toBytes: ToBytes[RegisteredProduct] =
    (registeredProduct: RegisteredProduct) => serialize(registeredProduct.asJson)

  implicit val fromBytes: FromBytes[RegisteredProduct] =
    (a: Array[Byte]) => deserialize[Json](a).as[RegisteredProduct].right.get
}