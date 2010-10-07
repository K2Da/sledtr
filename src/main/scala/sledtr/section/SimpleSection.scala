package sledtr.section
import net.htmlparser.jericho._
import scala.collection.JavaConversions._
import scala.collection.mutable._
import sledtr._
import sledtr.MyPreDef._
import sledtr.actors._
import sledtr.section.filter._
import sledtr.plugin._
import sledtr.shelf._
import sledtr.util._

object SimpleSection extends SectionCompanion {
  def apply(title: String, chapter: Chapter, url_list: List[String], map: ConfigMap): Section = {
    new SimpleSection(title, chapter, url_list, map)
  }
  override def canFormat(url: String): Boolean = true
}

class SimpleSection protected(title: String, chapter: Chapter, url_list: List[String], map: ConfigMap)
  extends Section(title, chapter, url_list, map) {
  val params: Map[String, PluginParam] = Map()
  
  lazy val src: Source = new Source(down_load.source)
  
  def targetElement: (Element) => Boolean = null
  
  private def cutHtml(): Element = {
    src.getAllElements.find { targetElement(_) } match {
      case Some(x) => x
      case None => throw new Throwable
    }
  }
  
  lazy val down_load = new HtmlDownload(url_list(0), Environ.srcDir + "/" + url_list(0).p)
  
  override def readSource(): Unit = SectionDownloadManager.addTask(down_load)
  
  def formatHtml(): String = {
    val sb = new StringBuffer
    
    if(targetElement != null)
      DefaultTag.getHtml(url_list(0), cutHtml(), sb)
    else
      DefaultTag.getHtml(url_list(0), findMaybeTarget, sb)
      
    sb.toString
  }
  
  def findMaybeTarget(): Element = {
    val divs = src.getAllElements.filter { e => e.getName == "div" }
    // id優先でdiv探します
    Collections.DivNames.foreach { n =>
      divs.find { d =>
        d.getAttributeValue("id") == n
      } match {
        case Some(d) => return d
        case _ =>
      }
      divs.find { d =>
        d.getAttributeValue("class") == n 
      } match {
        case Some(d) => return d
        case _ =>
      }
    }
    src.getAllElements.find { e => e.getName == "body" } match {
      case Some(b) => return b
      case _ => throw new Throwable("nai")
    }
  }
}