package sledtr.source
import sledtr.shelf._
import sledtr.MyPreDef._
import sledtr.plugin._
import scala.collection.mutable._

class UrlList private(chapter: Chapter, map: ConfigMap) extends Source(chapter, map) {
  val params: Map[String, PluginParam] = Map(
        "urllist" -> ListParam(List())
      )
  initParams()
  
  val urllist = params("urllist") match {
      case ListParam(l) => l
      case _ => throw new Throwable()
    }
  
  override def readSource(): Unit = { }
  
  override def createSection(): Unit = {
    urllist.foreach { url =>
      sections += new Section(chapter, "title", url)
    }
  }
}

object UrlList extends SourceCompanion {
  def apply(chapter: Chapter, map: ConfigMap): Source = {
    new UrlList(chapter, map)
  }
}