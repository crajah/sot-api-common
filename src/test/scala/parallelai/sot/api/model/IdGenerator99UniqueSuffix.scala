package parallelai.sot.api.model

object IdGenerator99UniqueSuffix extends IdGenerator99UniqueSuffix

trait IdGenerator99UniqueSuffix extends IdGenerator {
  override protected def uniqueSuffix: String = "99"
}