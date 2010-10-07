package sledtr.section.filter
import net.htmlparser.jericho._
import scala.collection.JavaConversions._
import sledtr.MyPreDef._
import sledtr._
import sledtr.actors._
import sledtr.config._

object IMG extends Tag {
  def getHtml(url: String, elm: Element, sb: StringBuffer): Unit = {
    elm.getAttributes().find { a => a.getKey == "src" } match {
      case Some(s) =>
        val ap = makeAbsolutePath(url, s.getValue)
        makeLocalFileName(ap) match {
          case Some(path) =>
            val d = new ImageDownload(ap, path)
            ImageDownloadManager.addTask(d)
            sb.append("<img src='%s' />".format(path.getAbsolutePath))
          case None =>
        }

      case None => "無いよ"
    }
  }
  
  private def makeAbsolutePath(url: String, p: String): String = {
    if(p.startsWith("http")) p
    else if(p.startsWith("/")) url.substring(0, url.indexOf("/", 8)) + p
    else url.substring(0, url.lastIndexOf("/") - 1) + p
  }
  
  def makeLocalFileName(ap: String): Option[File] = {
    val e = Array(".jpg", ".jpeg", ".gif", ".png", "bmp")
    
    def f(s: String) = {
      val l = s.toLowerCase
      if(e.contains(l))
        Some(new File(Environ.imageDir + "/" + ap.p + l))
      else None
    }
    
    """\.([^\./\?])*$""".r.findFirstIn(ap) match {
      case Some(s) => return f(s)
      case None =>
    }
    """\.([^\./\?])*\?""".r.findFirstIn(ap) match {
      case Some(s) => return f(s.substring(0, s.length - 1))
      case None => return None
    }
  }
}