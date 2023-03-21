package parallelai.sot.api.model

trait IdGenerator99UniqueSuffix extends IdGenerator {
  override protected def uniqueSuffix: String = "99"
}