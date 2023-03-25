package parallelai.sot.api.http.endpoints

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import io.finch.Output
import com.twitter.bijection.Conversion.asMethod
import com.twitter.bijection.twitter_util.UtilBijections._
import com.twitter.finagle.http.Status
import parallelai.sot.api.http.Result

trait EndpointOps {
  type TFuture[A] = com.twitter.util.Future[A]

  // TODO Monix Task, then have toTFuture that converts Task -> Future -> Twitter Future

  implicit class FutureResponseOps[T](f: Future[Response]) {
    def toOutput: Future[Output[Response]] = f recover {
      case ResponseException(r) => r
      case t: Throwable => Response(Response.Error(t.getMessage), Status.InternalServerError)
    } map { response =>
      Output.payload(response, response.status)
    }

    def toTFuture: TFuture[Output[Response]] = toOutput.as[TFuture[Output[Response]]]
  }

  implicit class ResponseOps(r: Response) {
    import cats.implicits._

    def toTFuture: TFuture[Output[Response]] = r.pure[Future].toTFuture
  }

  implicit class FutureResultOps[T](f: Future[Result[T]]) {
    def toOutput: Future[Output[Result[T]]] =
      f.map(r => Output.payload(r, r.status))

    def toTFuture: TFuture[Output[Result[T]]] =
      toOutput.as[TFuture[Output[Result[T]]]]
  }

  implicit class ResultOps[T](r: Result[T]) {
    import cats.implicits._

    def toTFuture: TFuture[Output[Result[T]]] = r.pure[Future].toTFuture
  }

  implicit class PathContext(val sc: StringContext) {
    def p(args: Any*): String =
      sc.s(args: _*).replaceAll(" ", "").replaceAll("::", "/")
  }
}