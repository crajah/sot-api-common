package parallelai.sot.api.model

import java.net.URI
import io.circe._
import io.circe.syntax._
import spray.json.DefaultJsonProtocol._
import spray.json._
import spray.json.lenses.JsonLenses._
import org.apache.commons.lang3.SerializationUtils.{deserialize, serialize}
import com.github.nscala_time.time.Imports._
import parallelai.common.secure.{FromBytes, ToBytes}

case class Version(value: String, token: Option[Token] = None, expiry: Option[DateTime] = None)

object Version {
  implicit val toBytes: ToBytes[Version] = serialize(_)

  implicit val fromBytes: FromBytes[Version] = deserialize[Version](_)

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

case class RegisteredVersion(uri: URI, version: String, token: Token, expiry: DateTime)

object RegisteredVersion {
  implicit val toBytes: ToBytes[RegisteredVersion] = serialize(_)

  implicit val fromBytes: FromBytes[RegisteredVersion] = deserialize[RegisteredVersion](_)

  implicit val encoder: Encoder[RegisteredVersion] = (v: RegisteredVersion) =>
    Json.obj(
      "uri" -> Json.fromString(v.uri.toString),
      "version" -> Json.fromString(v.version),
      "token" -> v.token.asJson,
      "expiry" -> Json.fromString(v.expiry.toString)
    )

  implicit val decoder: Decoder[RegisteredVersion] = (c: HCursor) => for {
    uri <- c.downField("uri").as[String]
    version <- c.downField("version").as[String]
    token <- c.downField("token").as[Token]
    expiry <- c.downField("expiry").as[String]
  } yield RegisteredVersion(new URI(uri), version, token, DateTime.parse(expiry))
}