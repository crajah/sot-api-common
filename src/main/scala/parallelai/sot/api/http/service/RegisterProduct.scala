package parallelai.sot.api.http.service

import cats.Monad
import parallelai.sot.api.http.Result
import parallelai.sot.api.model.{Product, RegisteredProduct}

abstract class RegisterProduct[F[_]: Monad] {
  def apply(product: Product): F[Result[RegisteredProduct]]
}