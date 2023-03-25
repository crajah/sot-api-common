package parallelai.sot.api.http.endpoints

import spray.json._
import org.scalatest.{MustMatchers, WordSpec}
import com.twitter.finagle.http.Status

class ResponseSpec extends WordSpec with MustMatchers with DefaultJsonProtocol {
  "Response" should {
    "be converted to JSON and back again (proving that there are implicit JSON reader and writer)" in {
      val response = Response("My content", Status.Accepted)

      response.toJson.convertTo[Response] mustEqual response
    }
  }
}