package parallelai.sot.api.model

import java.util.Base64._
import io.circe
import io.circe.{Decoder, Encoder, HCursor, Json}
import spray.json._
import org.apache.commons.lang3.SerializationUtils._
import parallelai.common.secure.Encrypted
import parallelai.common.secure.diffiehellman.ClientPublicKey

case class ProductRegister(organisation: Organisation, productToken: Encrypted, clientPublicKey: Option[ClientPublicKey] = None)

object ProductRegister extends DefaultJsonProtocol {
  implicit val rootJsonFormat: RootJsonFormat[ProductRegister] =
    jsonFormat3(ProductRegister.apply)

  implicit val clientPublicKeyEncoder: Encoder[ClientPublicKey] = (clientPublicKey: ClientPublicKey) => Json.obj(
    ("value", circe.Json.fromString(getEncoder encodeToString serialize(clientPublicKey.value)))
  )

  implicit val clientPublicKeyDecoder: Decoder[ClientPublicKey] = (hCursor: HCursor) => for {
    valueString <- hCursor.downField("value").as[String]
  } yield ClientPublicKey(getDecoder decode valueString)

  implicit val encoder: Encoder[ProductRegister] =
    Encoder.forProduct3("organisation", "productToken", "clientPublicKey")(p => (p.organisation, p.productToken, p.clientPublicKey))

  implicit val decoder: Decoder[ProductRegister] =
    Decoder.forProduct3("organisation", "productToken", "clientPublicKey")(ProductRegister.apply)
}