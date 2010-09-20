package sledtr.actors
import sledtr.util._
import sledtr.MyPreDef._

class HtmlDownload(url: String, save_path: String) extends Task() {
  val save = new File(save_path)
  var source: String = _
  override def myTask(): Unit = {
    source =
      if(save.exists)
        HttpReader.fromFile(save)
      else {
        var f = HttpReader.fromURL(url)
        FileUtil.write(save, f)
        f
      }
  }
  
  override def debugInfo(): String = "HTML download url:[%s] file:[%s]".format(url, save)
}