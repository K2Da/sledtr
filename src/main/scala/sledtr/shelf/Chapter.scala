package sledtr.shelf
import sledtr.source._
import sledtr.section._
import scala.collection.mutable.Map
import sledtr.MyPreDef._
import sledtr.util._

class Chapter(book: Book, map: ConfigMap) {
  val title = map.value("title")
  val source = Source.create(this, map.mapValue("source"))
  def sections = source.sections
  def getSection(title: String, url_list: List[String]) = Section.create(title, this, url_list, map.mapValue("section"))
  def readSource() = source.readSource()
  def createSection() =     try {
      source.createSection()
    } catch {
       case e =>
         e.printStackTrace
         log(e)
    }
  override def toString = title + "\n  " + source.toString + "\n  " + map.mapValue("section")
}

object Chapter {
  def apply(book: Book, config: ConfigArray) =
    config.map { c => new Chapter(book, c.toMapValue) }
}