package parallelai.sot.api.model

import javax.crypto.SecretKey
import org.apache.commons.lang3.SerializationUtils.{deserialize, serialize}
import parallelai.common.secure.{FromBytes, ToBytes}

case class ApiSharedSecret(licenceId: String, apiServerSecret: SecretKey)

object ApiSharedSecret {
  implicit val toBytes: ToBytes[ApiSharedSecret] = serialize(_)

  implicit val fromBytes: FromBytes[ApiSharedSecret] = deserialize[ApiSharedSecret](_)
}