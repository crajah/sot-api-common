package parallelai.sot.api.http.service

import cats.Monad
import parallelai.sot.api.http.Result
import parallelai.sot.api.model.{Organisation, RegisteredOrganisation}

abstract class RegisterOrganisation[F[_]: Monad] {
  def apply(organisation: Organisation): F[Result[RegisteredOrganisation]]
}