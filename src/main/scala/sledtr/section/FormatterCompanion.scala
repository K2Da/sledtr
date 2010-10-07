package sledtr.section
import sledtr.plugin._
import sledtr.shelf._
import sledtr.MyPreDef._

abstract class SectionCompanion extends SelectedCompanion[Section] {
  def apply(title: String, chapter: Chapter, url_list: List[String], map: ConfigMap): Section
  def canFormat(url: String): Boolean
}