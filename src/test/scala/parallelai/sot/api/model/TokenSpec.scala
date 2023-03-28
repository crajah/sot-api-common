package parallelai.sot.api.model

import io.circe.syntax._
import org.scalatest.{MustMatchers, WordSpec}
import parallelai.common.secure.{AES, Crypto, CryptoMechanic, Encrypted}

class TokenSpec extends WordSpec with MustMatchers {
  implicit val crypto: CryptoMechanic = new CryptoMechanic(AES, secret = "victorias secret".getBytes)

  "Token" should {
    "be encrypted and decrypted" in {
      val token = Token("id", "code", "email", Option(Crypto.aesSecretKey))

      Encrypted(token).decrypt mustEqual token
    }

    "be converted to JSON and back again" in {
      val token = Token("id", "code", "email", Option(Crypto.aesSecretKey))

      token.asJson.as[Token].right.get mustEqual token
    }
  }
}