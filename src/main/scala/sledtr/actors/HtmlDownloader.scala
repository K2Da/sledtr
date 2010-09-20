package sledtr.actors
import sledtr.MyPreDef._
import sledtr.util._
import sledtr._

class ImageDownload(url: String, save: File) extends Task() {
  override def myTask(): Unit = {
    HttpReader.getImage(url, save)
  }
  
  override def debugInfo(): String = "Image download url:[%s] file:[%s]".format(url, save)
}

object HtmlDownloadManager extends Manager(3, 500) { }

object ImageDownloadManager extends Manager(10, 0) { }