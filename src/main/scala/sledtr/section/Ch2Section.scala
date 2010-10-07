package sledtr.section
import scala.collection.mutable._
import sledtr._
import sledtr.MyPreDef._
import sledtr.actors._
import sledtr.plugin._
import sledtr.shelf._
import java.util.Date

object Ch2Section extends SectionCompanion {
  def apply(title: String, chapter: Chapter, url_list: List[String], map: ConfigMap): Section = {
    new Ch2Section(title, chapter, url_list, map)
  }
  override def canFormat(url: String): Boolean = false
}

class Ch2Section protected(title: String, chapter: Chapter, url_list: List[String], map: ConfigMap)
  extends Section(title, chapter, url_list, map) {
  val params: Map[String, PluginParam] = Map(
        "ngword" -> ListParam(List())
      )
  initParams()
  
  val ngword = getListParam("ngword")
  var down: List[HtmlDownload] = _
  var url_map: Map[String, String] = _
  
  override def readSource(): Unit = {
    down = for(u <- url_list) yield new HtmlDownload(u, Environ.srcDir + "/" + u.p)
    down.foreach { d => SectionDownloadManager.addTask(d) }
  }
  
  def createRes(): List[Ch2Res] = {
    var list = new ListBuffer[Ch2Res]
    down.foreach { d =>
      d.source.split("\n").zipWithIndex.foreach { case (line, no) =>
        Ch2Res(d.url, no + 1, line) match {
          case Some(r) => list += r
          case _ => log("henna res ga atta")
        }
      }
    }
    list.toList
  }
  
  def formatHtml(): String = {
    val list = createRes
    val sb = new StringBuffer
    
    val before = list.hourFilter(26)
    val after = before.ngFilter(ngword)
    sb.append("総対象レス数 %1$d NG数 %2$d".format(before.size, before.size - after.size))
    after.resSort.foreach { r => sb.append(r.format) }
    sb.toString
  }
  
  implicit def List2ResList(res: List[Ch2Res]): ResList = new ResList(res)
}

class ResList(res: List[Ch2Res]) {
  def hourFilter(hour: Int): List[Ch2Res] = res.filter { r => r.inNhour(hour) }
  def resSort: List[Ch2Res] = res.sortWith { (a, b) => a.date before b.date }
  
  def ngFilter(ngword: List[String]): List[Ch2Res] = {
    res.filter { r => ! r.isNG(ngword) }
  }
}