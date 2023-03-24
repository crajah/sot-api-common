package parallelai.sot.api.model

import io.circe.parser._
import io.circe.syntax._
import org.scalatest.{MustMatchers, WordSpec}
import parallelai.common.secure.{AES, CryptoMechanic, Encrypted}

class OrganisationSpec extends WordSpec with MustMatchers {
  implicit val crypto: CryptoMechanic = new CryptoMechanic(AES, secret = "victorias secret".getBytes)

  "Organisation" should {
    "be encrypted and decrypted" in {
      val organisation = Organisation("id", "code", "email")

      Encrypted(organisation).decryptT[Organisation] mustEqual organisation
    }

    "be decoded" in {
      val Right(organisationPostedJson) = parse(s"""{ "org_code":"SOME CODE", "org_email":"someone@gmail.com" }""")

      organisationPostedJson.as[Organisation] must matchPattern {
        case Right(Organisation(_, "SOME CODE", "someone@gmail.com")) =>
      }
    }

    "be encoded" in {
      val organisation = Organisation("id", "SOME CODE", "someone@gmail.com")
      val json = organisation.asJson

      json.as[Organisation] must matchPattern {
        case Right(Organisation(_, "SOME CODE", "someone@gmail.com")) =>
      }
    }
  }
}