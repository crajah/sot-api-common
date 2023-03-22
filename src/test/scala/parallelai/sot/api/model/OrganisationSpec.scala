package parallelai.sot.api.model

import org.scalatest.{MustMatchers, WordSpec}
import parallelai.common.secure.{CryptoMechanic, Encrypted}

class OrganisationSpec extends WordSpec with MustMatchers {
  implicit val crypto: CryptoMechanic = new CryptoMechanic(secret = "victorias secret".getBytes)

  "Organisation" should {
    "be encypted and decrypted" in {
      val organisation = Organisation("id", "code", "email")

      Encrypted(organisation).decryptT[Organisation] mustEqual organisation
    }
  }
}