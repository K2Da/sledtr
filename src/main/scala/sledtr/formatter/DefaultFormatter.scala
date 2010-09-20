package sledtr.formatter
import sledtr._
import sledtr.MyPreDef._
import sledtr.shelf._
import scala.collection.mutable.Map
import sledtr.plugin._

object DefaultFormatter extends FormatterCompanion {
  def apply(chapter: Chapter, section: Section, map: ConfigMap): Formatter = {
    Formatter.list.find { c => c.canFormat(section.url) } match {
      case Some(f) =>
        f(chapter, section, map)
      case _       =>
        SimpleFormatter(chapter, section, map)
    }
  }
  
  def canFormat(url: String) = false
}
