package sledtr.shelf
import sledtr.source._
import sledtr.formatter._
import scala.collection.mutable.Map
import sledtr.MyPreDef._
import sledtr.util._

class Chapter(book: Book, map: ConfigMap) {
  val title = map.value("title")
  val source = Source.create(this, map.mapValue("source"))
  def sections = source.sections
  def getFormatter(section: Section) = Formatter.create(this, section, map.mapValue("formatter"))
  def readSource() = source.readSource()
  def createSection() =     try {
      source.createSection()
    } catch {
       case e =>
         e.printStackTrace
         log(e)
    }
  override def toString = title + "\n  " + source.toString + "\n  " + map.mapValue("formatter")
}

object Chapter {
  def apply(book: Book, config: ConfigArray) =
    config.map { c => new Chapter(book, c.toMapValue) }
}