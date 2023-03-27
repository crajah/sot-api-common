package parallelai.sot.api.model

import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}
import org.apache.commons.lang3.SerializationUtils._
import parallelai.common.secure.{FromBytes, ToBytes}

case class Token(id: String, code: String, email: String)

object Token {
  implicit val toBytes: ToBytes[Token] = serialize(_)

  implicit val fromBytes: FromBytes[Token] = deserialize[Token](_)

  implicit val encoder: Encoder[Token] = deriveEncoder

  implicit val decoder: Decoder[Token] = deriveDecoder
}