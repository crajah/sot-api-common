package parallelai.sot.api.model

import io.circe.parser._
import io.circe.syntax._
import org.scalatest.{MustMatchers, WordSpec}
import parallelai.common.secure.{AES, CryptoMechanic, Encrypted}

class TokenSpec extends WordSpec with MustMatchers {
  implicit val crypto: CryptoMechanic = new CryptoMechanic(AES, secret = "victorias secret".getBytes)

  "Token" should {
    "be encrypted and decrypted" in {
      val organisation = Token("id", "code", "email")

      Encrypted(organisation).decrypt mustEqual organisation
    }

    "be decoded" in {
      val Right(organisationPostedJson) = parse(s"""{ "org_code":"SOME CODE", "org_email":"someone@gmail.com" }""")

      organisationPostedJson.as[Token] must matchPattern {
        case Right(Token(_, "SOME CODE", "someone@gmail.com")) =>
      }
    }

    "be encoded" in {
      val organisation = Token("id", "SOME CODE", "someone@gmail.com")
      val json = organisation.asJson

      json.as[Token] must matchPattern {
        case Right(Token(_, "SOME CODE", "someone@gmail.com")) =>
      }
    }
  }
}