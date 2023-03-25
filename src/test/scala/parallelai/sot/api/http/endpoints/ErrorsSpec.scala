package parallelai.sot.api.http.endpoints

import org.scalatest.{MustMatchers, WordSpec}
import spray.json._

class ErrorsSpec extends WordSpec with MustMatchers {
  "Errors" should {
    "be converted to JSON with 1 error message" in {
      Response.Errors("blah").toJson mustEqual JsObject(
        "error-messages" -> JsArray(JsString("blah"))
      )
    }

    "be converted to JSON with 3 error messages" in {
      Response.Errors("blah-1", "blah-2", "blah-3").toJson mustEqual JsObject(
        "error-messages" -> JsArray(JsString("blah-1"), JsString("blah-2"), JsString("blah-3"))
      )
    }
  }
}