package sledtr.formatter.sites
import sledtr.formatter._
import sledtr._
import sledtr.shelf._
import sledtr.MyPreDef._
import sledtr.plugin._
import scala.collection.mutable._
import net.htmlparser.jericho._
import scala.util.matching._

object SimpleExtender extends FormatterCompanion {
  def apply(chapter: Chapter, section: Section, map: ConfigMap): Formatter = {
    new SimpleExtender(chapter, section, map)
  }
  
  val sites: List[Tuple2[Regex, (Element) => Boolean]] = Collections.SimpleExtenderSites
  
  override def canFormat(url: String): Boolean = {
    getSite(url) match {
      case Some(x) => true
      case _ => false
    }
  }
  
  def getSite(url: String): Option[Tuple2[Regex, (Element) => Boolean]] = {
    sites.find { t =>
      t._1.findFirstIn(url) match {
        case Some(x) => true
        case _ => false
      }
    }
  }
}

class SimpleExtender protected (chapter: Chapter, section: Section, map: ConfigMap)
  extends SimpleFormatter(chapter, section, map) {
  
  override def targetElement: (Element) => Boolean = {
    SimpleExtender.getSite(section.url) match {
      case Some(x) => x._2
      case _ => throw new Throwable("nai")
    }
  }
}