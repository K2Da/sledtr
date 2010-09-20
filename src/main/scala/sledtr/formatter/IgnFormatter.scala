package sledtr.formatter
import sledtr.shelf._
import sledtr.MyPreDef._
import scala.collection.mutable._
import sledtr.plugin._

object IgnFormatter extends FormatterCompanion {
  def apply(chapter: Chapter, section: Section, map: ConfigMap): Formatter = {
    new IgnFormatter(chapter, section, map)
  }
  override def canFormat(url: String) = false
}

class IgnFormatter private(chapter: Chapter, section: Section, map: ConfigMap)
  extends Formatter(chapter, section, map) {
  val params: Map[String, PluginParam] = Map()
  
  override def readSource(): Unit = {}
  
  override def formatHtml(): String = { "" }
}