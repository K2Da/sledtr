package sledtr.formatter
import sledtr._
import sledtr.shelf._
import sledtr.util._
import sledtr.MyPreDef._
import sledtr.formatter.filter._
import sledtr.actors._
import sledtr.plugin._
import net.htmlparser.jericho._
import scala.collection.mutable._
import scala.collection.JavaConversions._

object SimpleFormatter extends FormatterCompanion {
  def apply(chapter: Chapter, section: Section, map: ConfigMap): Formatter = {
    new SimpleFormatter(chapter, section, map)
  }
  override def canFormat(url: String): Boolean = true
}

class SimpleFormatter protected(chapter: Chapter, section: Section, map: ConfigMap)
  extends Formatter(chapter, section, map) {
  val params: Map[String, PluginParam] = Map()
  
  lazy val src: Source = new Source(down_load.source)
  
  def targetElement: (Element) => Boolean = null
  
  private def cutHtml(): Element = {
    src.getAllElements.find { targetElement(_) } match {
      case Some(x) => x
      case None => throw new Throwable
    }
  }
  
  lazy val down_load = new HtmlDownload(section.url, Environ.srcDir + "/" + section.url.p)
  
  override def readSource(): Unit = SectionDownloadManager.addTask(down_load)
  
  def formatHtml(): String = {
    val sb = new StringBuffer
    
    if(targetElement != null)
      DefaultTag.getHtml(section.url, cutHtml(), sb)
    else
      DefaultTag.getHtml(section.url, findMaybeTarget, sb)
      
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