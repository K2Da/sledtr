package sledtr.section.sites
import sledtr.section._
import sledtr._
import sledtr.shelf._
import sledtr.MyPreDef._
import sledtr.plugin._
import scala.collection.mutable._
import net.htmlparser.jericho._
import scala.util.matching._

object SimpleExtender extends SectionCompanion {
  def apply(title: String, chapter: Chapter, url_list: List[String], map: ConfigMap): Section = {
    new SimpleExtender(title, chapter, url_list, map)
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

class SimpleExtender protected (title: String, chapter: Chapter, url_list: List[String], map: ConfigMap)
  extends SimpleSection(title, chapter, url_list, map) {
  
  override def targetElement: (Element) => Boolean = {
    SimpleExtender.getSite(url_list(0)) match {
      case Some(x) => x._2
      case _ => throw new Throwable("nai")
    }
  }
}