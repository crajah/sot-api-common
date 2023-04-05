package parallelai.sot.api.file

import parallelai.sot.api.model.{RegisteredVersion, Version}

trait GCFileNameConverter[T] {
  def defineFileName(t: T): String
}

object GCFileNameConverter {
  val suffix = "-parallelai-sot.zip"

  implicit object VersionConverter extends GCFileNameConverter[Version] {
    override def defineFileName(t: Version): String =
      s"${t.token.map(_.id).getOrElse("")}-${t.value}${suffix}"
  }

  implicit object RegisteredConverter extends GCFileNameConverter[RegisteredVersion] {
    override def defineFileName(t: RegisteredVersion): String =
      s"${t.token.id}-${t.version}${suffix}"
  }

  implicit class FileNameUtil[T](x: T) {
    def defineFileName(implicit makesName: GCFileNameConverter[T]): String = makesName.defineFileName(x)
  }
}