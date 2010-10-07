package sledtr.section
import sledtr.section.sites._
import sledtr._
import sledtr.shelf._
import scala.collection.mutable._
import sledtr.MyPreDef._
import sledtr.plugin._

object Section extends Selector[SectionCompanion, Section] {
  val list: List[SectionCompanion] = Collections.SectionList
    
  def create(title: String, chapter: Chapter, url_list: List[String], map: ConfigMap): Section = {
    this.select(map) match {
      case f: SectionCompanion => f(title, chapter, url_list, map)
      case _ => throw new Throwable
    }
  }
}

abstract class Section(val title: String, val chapter: Chapter, val url_list: List[String], map: ConfigMap)
  extends Selected(map) {
  override def toString(): String = {
    val c = getClass
    c.getName.replace(c.getPackage.getName + ".", "")
  }
  
  def readSource(): Unit
  
  def formatHtml(): String
}