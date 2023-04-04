package parallelai.sot.api.model

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}


case class ZipToken(licenceId: String, version: String)

object ZipToken {
  implicit val encoder: Encoder[ZipToken] = deriveEncoder
  implicit val decoder: Decoder[ZipToken] = deriveDecoder
}
