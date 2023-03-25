package parallelai.sot.api.model

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import parallelai.common.secure.Encrypted
import parallelai.common.secure.diffiehellman.ServerPublicKey

case class RegisteredProduct(serverPublicKey: ServerPublicKey, licenceIdAndApiServerSecret: Encrypted)

object RegisteredProduct {
  implicit val encoder: Encoder[RegisteredProduct] = deriveEncoder

  implicit val decoder: Decoder[RegisteredProduct] = deriveDecoder
}