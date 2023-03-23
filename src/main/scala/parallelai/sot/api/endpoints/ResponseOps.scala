package parallelai.sot.api.endpoints

import spray.json._

trait ResponseOps {
  implicit val response2String: Response => String =
    _.toJson.prettyPrint
}