package parallelai.sot.api.json

import spray.json.DefaultJsonProtocol._
import spray.json._
import spray.json.lenses.JsonLenses._
import org.scalatest.{MustMatchers, WordSpec}
import parallelai.sot.api.json.JsonLens._

class JsonLensSpec extends WordSpec with MustMatchers {
  "Json lenses" should {
    "find an optional field" in {
      val json = JsObject(
        "id" -> JsString("my-id"),
        "name" -> JsString("my-name")
      )

      json.extract[String]('id.?) mustBe Some("my-id")
    }

    "not find an optional field" in {
      val json = JsObject(
        "name" -> JsString("my-name")
      )

      json.extract[String]('id.?) mustBe None
    }

    "find all required fields" in {
      val json = JsObject(
        "id" -> JsString("my-id"),
        "name" -> JsString("my-name")
      )

      json.containsFields("id", "name") mustBe true
    }

    "not find all required fields" in {
      val json = JsObject(
        "id" -> JsString("my-id")
      )

      json.containsFields("id", "name") mustBe false
    }

    "find all required fields when none are actually given" in {
      val json = JsObject(
        "id" -> JsString("my-id")
      )

      json.containsFields() mustBe true
    }

    "not find all required fields for an empty JSON" in {
      val json = JsNull

      json.containsFields("id", "name") mustBe false
    }
  }
}