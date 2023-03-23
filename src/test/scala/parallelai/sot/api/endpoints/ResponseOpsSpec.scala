package parallelai.sot.api.endpoints

import spray.json._
import org.scalatest.{MustMatchers, WordSpec}
import com.twitter.finagle.http.Status

class ResponseOpsSpec extends WordSpec with MustMatchers with DefaultJsonProtocol with ResponseOps {
  "Response" should {
    "be implicitly converted to a string" in {
      val response = Response("My content", Status.Accepted)

      val responseString: String = response

      responseString must include("My content")
    }
  }
}