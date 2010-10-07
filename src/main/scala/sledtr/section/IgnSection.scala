package sledtr.section
import sledtr.shelf._
import sledtr.MyPreDef._
import scala.collection.mutable._
import sledtr.plugin._

object IgnSection extends SectionCompanion {
  def apply(title: String, chapter: Chapter, url_list: List[String], map: ConfigMap): Section = {
    new IgnSection(title, chapter, url_list, map)
  }
  override def canFormat(url: String) = false
}

class IgnSection private(title: String, chapter: Chapter, url_list: List[String], map: ConfigMap)
  extends Section(title, chapter, url_list, map) {
  val params: Map[String, PluginParam] = Map()
  
  override def readSource(): Unit = {}
  
  override def formatHtml(): String = { "" }
}