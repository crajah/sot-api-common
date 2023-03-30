package parallelai.sot.api.model

import io.circe.syntax._
import org.scalatest.{Inside, MustMatchers, WordSpec}
import com.github.nscala_time.time.Imports._
import parallelai.common.secure.{AES, Crypto, Encrypted}

class VersionSpec extends WordSpec with MustMatchers with Inside {
  implicit val crypto: Crypto = new Crypto(AES, "victorias secret".getBytes)

  "Version" should {
    "be converted to/from JSON with no token and expiry" in {
      val version = Version("1.1.4")

      version.asJson.as[Version].right.get must have (
        'value ("1.1.4"),
        'token (None),
        'expiry (None)
      )
    }

    "be converted to/from JSON with no expiry" in {
      val token = Token("id", "code", "email")
      val version = Version("1.1.4", Option(token))

      version.asJson.as[Version].right.get must have (
        'value ("1.1.4"),
        'token (Option(token)),
        'expiry (None)
      )
    }

    "be converted to/from JSON" in {
      val token = Token("id", "code", "email")
      val nextDay = DateTime.nextDay

      val version = Version("1.1.4", Option(token), Option(nextDay))

      inside(version.asJson.as[Version]) {
        case Right(Version("1.1.4", Some(`token`), Some(nd))) =>
          nd.getMillis mustEqual nextDay.getMillis
      }
    }

    "be encrypted and decrypted" in {
      val token = Token("id", "code", "email")
      val nextDay = DateTime.nextDay

      val version = Version("1.1.4", Option(token), Option(nextDay))

      Encrypted(version).decrypt mustEqual version
    }
  }
}