package parallelai.sot.api.model

import org.apache.commons.lang3.SerializationUtils.{deserialize, serialize}
import parallelai.common.secure.{FromBytes, ToBytes}

case class Token(licenceId: String, code: String, email: String)

object Token {
  implicit val tokenToBytes: ToBytes[Token] = serialize(_)

  implicit val tokenFromBytes: FromBytes[Token] = deserialize[Token](_)
}