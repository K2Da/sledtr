package sledtr.source
import scala.collection.mutable.Map
import sledtr.actors._
import sledtr.shelf._
import sledtr.MyPreDef._
import sledtr.config._
import sledtr._
import scala.xml._
import sledtr.plugin._

class RssFeed private(chapter: Chapter, map: ConfigMap) extends Source(chapter, map) {
  val params: Map[String, PluginParam] = Map(
        "url" -> StringParam(None)
      )
  initParams()
  
  val url = getStringParam("url")
  
  lazy val down_load = new HtmlDownload(url, Environ.srcDir + "/" + url.p)
  
  override def readSource(): Unit = HtmlDownloadManager.addTask(down_load)
  
  override def createSection(): Unit = {
    val xml = XML.loadString(down_load.source)
    (xml \\ "item").foreach { n =>
      sections += chapter.getSection((n \ "title").text, (n \ "link").text :: Nil)
    }
  }
}

object RssFeed extends SourceCompanion {
  def apply(chapter: Chapter, map: ConfigMap): Source = {
    new RssFeed(chapter, map)
  }
}