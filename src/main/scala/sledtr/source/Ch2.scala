package sledtr.source
import scala.collection.mutable.Map
import sledtr.shelf._
import sledtr.section._
import sledtr.MyPreDef._
import sledtr.plugin._
import sledtr.actors._
import sledtr._
import scala.xml._

class Ch2 private(chapter: Chapter, map: ConfigMap) extends Source(chapter, map) {
  val params: Map[String, PluginParam] = Map(
        "url"     -> StringParam(None),
        "keyword" -> StringParam(None)
      )
  initParams()
  
  val url = getStringParam("url")
  val keyword = getStringParam("keyword")
  
  lazy val down_load = new HtmlDownload(url + "subject.txt", Environ.srcDir + "/" + url.p)
  
  override def readSource(): Unit = HtmlDownloadManager.addTask(down_load)
  
  override def createSection(): Unit = {
    var url_map: Map[String, String] = Map()
    down_load.source.split("\n").foreach { s =>
      val (title, surl) = getTitleUrl(s)
      if(keyword.r.findFirstIn(title) != None) url_map(surl) = title
    }
    
    val url_list = url_map.toList.map { case(a, b) => a }
    val section = chapter.getSection(keyword, url_list)
    (section: @unchecked) match {
      case s: Ch2Section => s.url_map = url_map
    }
    sections += section
  }
  
  def getTitleUrl(line: String): Tuple2[String, String] = {
    var r = """ \(\d+\)""".r
    val a = line.split("<>")
    (r.replaceAllIn(a(1), ""), url + "dat/" + a(0))
  }
}

object Ch2 extends SourceCompanion {
  def apply(chapter: Chapter, map: ConfigMap): Source = { new Ch2(chapter, map) }
}