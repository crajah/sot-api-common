package parallelai.sot.api.http

import io.circe.syntax._
import org.scalatest.{MustMatchers, WordSpec}

class ErrorsSpec extends WordSpec with MustMatchers {
  "Errors" should {
    "be converted to JSON and back" in {
      println(Errors("blah").asJson.as[Errors])
      println(Errors("blah", "blah again").asJson.as[Errors])

      // TODO - WIP
    }
  }
}