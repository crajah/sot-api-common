package parallelai.sot.api.model

import org.scalatest.{MustMatchers, WordSpec}
import parallelai.common.secure.{CryptoMechanic, Encrypted}
import io.circe.parser._
import io.circe.syntax._

class OrganisationSpec extends WordSpec with MustMatchers {
  implicit val crypto: CryptoMechanic = new CryptoMechanic(secret = "victorias secret".getBytes)

  "Organisation" should {
    "be encrypted and decrypted" in {
      val organisation = Organisation("id", "code", "email", "secret")

      Encrypted(organisation).decryptT[Organisation] mustEqual organisation
    }

    "be decoded" in {
      val Right(organisationPostedJson) = parse(s"""{ "org_code":"SOME CODE", "org_email":"someone@gmail.com", "org_shared_secret":"secret" }""")

      organisationPostedJson.as[Organisation] must matchPattern {
        case Right(Organisation(_, "SOME CODE", "someone@gmail.com", "secret")) =>
      }
    }

    "be encoded" in {
      val organisation = Organisation("id", "SOME CODE", "someone@gmail.com", "secret")
      val json = organisation.asJson

      json.as[Organisation] must matchPattern {
        case Right(Organisation(_, "SOME CODE", "someone@gmail.com", "secret")) =>
      }
    }
  }
}