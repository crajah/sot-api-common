package parallelai.sot.api.model

import io.circe.syntax._
import org.scalatest.{MustMatchers, WordSpec}
import parallelai.common.secure.{AES, Crypto, Encrypted}

class TokenSpec extends WordSpec with MustMatchers {
  implicit val crypto: Crypto = new Crypto(AES, "victorias secret".getBytes)

  "Token" should {
    "be encrypted and decrypted" in {
      val token = Token("id", "code", "email")

      Encrypted(token).decrypt mustEqual token
    }

    "be converted to JSON and back again" in {
      val token = Token("id", "code", "email")

      token.asJson.as[Token].right.get mustEqual token
    }
  }
}