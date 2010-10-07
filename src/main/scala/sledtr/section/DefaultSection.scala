package sledtr.section
import sledtr._
import sledtr.MyPreDef._
import sledtr.shelf._
import scala.collection.mutable.Map
import sledtr.plugin._

object DefaultSection extends SectionCompanion {
  def apply(title: String, chapter: Chapter, url_list: List[String], map: ConfigMap): Section = {
    Section.list.find { c => c.canFormat(url_list(0)) } match {
      case Some(f) =>
        f(title, chapter, url_list, map)
      case _       =>
        SimpleSection(title, chapter, url_list, map)
    }
  }
  
  def canFormat(url: String) = false
}
