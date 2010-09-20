package sledtr.source
import sledtr.plugin._
import sledtr.shelf._
import sledtr.MyPreDef._

abstract class SourceCompanion extends SelectedCompanion[Source] {
  def apply(chapter: Chapter, map: ConfigMap): Source
}
