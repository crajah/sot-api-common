package parallelai.sot.api.model

import monocle.macros.GenLens
import org.scalatest.{MustMatchers, WordSpec}
import parallelai.common.secure.{AES, Crypto, Encrypted}

class OrganisationSpec extends WordSpec with MustMatchers {
  implicit val crypto: Crypto = Crypto(AES, "victorias secret".getBytes)

  "Organisation" should {
    "have token and secret added" in {
      val organisation = Organisation("code", "email", None, None)

      organisation must have (
        'token (None),
        'secret (None)
      )

      val token = GenLens[Organisation](_.token).set(Option(Encrypted(Token("id", "code", "email"))))
      val secret = GenLens[Organisation](_.secret).set(Option(Crypto.aesSecretKey))

      (token compose secret)(organisation) must matchPattern { case Organisation("code", "email", Some(_), Some(_)) => }
    }
  }
}