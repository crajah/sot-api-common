package parallelai.sot.api.file

import java.net.URI

import com.github.nscala_time.time.Imports.DateTime
import org.scalatest.{MustMatchers, WordSpec}
import parallelai.sot.api.model.{RegisteredVersion, Token, Version}

class GCFileNameConverterSpec extends WordSpec with MustMatchers {

  "can define the file name to upload/download from google cloud" in {
    import GCFileNameConverter._
    val uri = new URI("http://parallelai.com/registered-version")
    val versionNumber = "v1.0.0"
    val token = Token("licenceId", "organisationCode", "me@gmail.com")
    val nextDay = DateTime.nextDay
    val registeredVersion = RegisteredVersion(uri, versionNumber, token, nextDay)
    val version = Version(versionNumber, Option(token))

    registeredVersion.defineFileName mustEqual "licenceId-v1.0.0-parallelai-sot.zip"
    version.defineFileName mustEqual "licenceId-v1.0.0-parallelai-sot.zip"
    registeredVersion.defineFileName mustEqual version.defineFileName
  }
}
