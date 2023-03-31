package parallelai.sot.api.http.service

import cats.Monad
import parallelai.common.secure.Encrypted
import parallelai.sot.api.http.Result
import parallelai.sot.api.model.{RegisteredVersion, Version}

abstract class RegisterVersion[F[_]: Monad] {
  def apply(version: Encrypted[Version]): F[Result[Encrypted[RegisteredVersion]]]
}