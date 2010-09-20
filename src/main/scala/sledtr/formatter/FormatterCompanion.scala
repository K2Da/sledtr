package sledtr.formatter
import sledtr.plugin._
import sledtr.shelf._
import sledtr.MyPreDef._

abstract class FormatterCompanion extends SelectedCompanion[Formatter] {
  def apply(chapter: Chapter, section: Section, map: ConfigMap): Formatter
  def canFormat(url: String): Boolean
}