package sledtr.shelf
import sledtr.formatter.Formatter
import sledtr.actors._
import sledtr.MyPreDef._

class Section(chapter: Chapter, val title: String, val url: String) {
  val formatter = chapter.getFormatter(this)

  def readSource() = formatter.readSource()
  
  def formatHtml(): String = {
    try {
      return formatter.formatHtml()
    } catch {
      case e =>
        log(e)
        e.printStackTrace()
        return ""
      }
  }
}