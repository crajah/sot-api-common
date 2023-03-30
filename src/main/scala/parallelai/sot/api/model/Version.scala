package parallelai.sot.api.model

import io.circe._
import io.circe.generic.semiauto._
import io.circe.syntax._
import spray.json.DefaultJsonProtocol._
import spray.json._
import spray.json.lenses.JsonLenses._
import org.apache.commons.lang3.SerializationUtils.{deserialize, serialize}
import com.github.nscala_time.time.Imports._
import parallelai.common.secure.{FromBytes, ToBytes}

case class Version(value: String, token: Option[Token] = None, expiry: Option[DateTime] = None)

object Version {
  implicit val toBytes: ToBytes[Version] =
    (version: Version) => serialize(version)

  implicit val fromBytes: FromBytes[Version] =
    (a: Array[Byte]) => deserialize[Version](a)

  implicit val rootJsonFormat: RootJsonFormat[Version] = new RootJsonFormat[Version] {
    def write(version: Version): JsValue = JsObject("version" -> JsString(version.value))

    def read(json: JsValue): Version = Version(json.extract[String]("version"))
  }

  implicit val dateTimeEncoder: Encoder[DateTime] =
    (dateTime: DateTime) => Json.fromString(dateTime.toString)

  implicit val dateTimeDecoder: Decoder[DateTime] =
    (c: HCursor) => c.last.as[String].map(DateTime.parse)

  implicit val encoder: Encoder[Version] = (version: Version) => {
    val json = Json.obj("version" -> Json.fromString(version.value))

    val token: Option[Json] =
      version.token.map(c => Json.obj("token" -> c.asJson))

    val expiry: Option[Json] =
      version.expiry.map(expiry => Json.obj("expiry" -> Json.fromString(expiry.toString)))

    Seq(token, expiry).flatten.foldLeft(json) { (acc, json) => json deepMerge acc }
  }

  implicit val decoder: Decoder[Version] = (c: HCursor) => for {
    version <- c.downField("version").as[String]
    token <- c.downField("token").as[Option[Token]]
    expiry <- c.downField("expiry").as[Option[String]]
  } yield Version(version, token, expiry.map(DateTime.parse))
}

case class VersionActive(version: String, active: Boolean)

object VersionActive {
  implicit val rootJsonFormat: RootJsonFormat[VersionActive] = jsonFormat2(VersionActive.apply)
}

case class Versions(versions: Seq[VersionActive])

object Versions {
  implicit val rootJsonFormat: RootJsonFormat[Versions] = jsonFormat1(Versions.apply)
}

case class RegisteredVersion()

object RegisteredVersion {
  implicit val encoder: Encoder[RegisteredVersion] = deriveEncoder[RegisteredVersion]

  implicit val decoder: Decoder[RegisteredVersion] = deriveDecoder[RegisteredVersion]
}