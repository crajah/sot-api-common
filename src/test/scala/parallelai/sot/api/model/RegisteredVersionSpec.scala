package parallelai.sot.api.model

import java.net.URI
import io.circe.syntax._
import org.scalatest.{Inside, MustMatchers, WordSpec}
import com.github.nscala_time.time.Imports._
import parallelai.common.secure.{AES, Crypto, Encrypted}

class RegisteredVersionSpec extends WordSpec with MustMatchers with Inside {
  implicit val crypto: Crypto = new Crypto(AES, "victorias secret".getBytes)

  "Registered version" should {
    "be converted to/from JSON" in {
      val uri = new URI("http://parallelai.com/registered-version")
      val version = "v1.0.0"
      val token = Token("licenceId", "organisationCode", "me@gmail.com")
      val nextDay = DateTime.nextDay
      val registeredVersion = RegisteredVersion(uri, version, token, nextDay)

      inside(registeredVersion.asJson.as[RegisteredVersion]) {
        case Right(RegisteredVersion(`uri`, `version`, `token`, nd)) =>
          nd.getMillis mustEqual nextDay.getMillis
      }
    }

    "be encrypted and decrypted" in {
      val uri = new URI("http://parallelai.com/registered-version")
      val version = "v1.0.0"
      val token = Token("licenceId", "organisationCode", "me@gmail.com")
      val nextDay = DateTime.nextDay
      val registeredVersion = RegisteredVersion(uri, version, token, nextDay)

      Encrypted(registeredVersion).decrypt mustEqual registeredVersion
    }

    "define and upload/download file name convention" in {
      val uri = new URI("http://parallelai.com/registered-version")
      val version = "v1.0.0"
      val token = Token("licenceId", "organisationCode", "me@gmail.com")
      val nextDay = DateTime.nextDay
      val registeredVersion = RegisteredVersion(uri, version, token, nextDay)

      registeredVersion.defineFileName mustEqual "licenceId-v1.0.0-parallelai-sot.zip"
    }
  }
}