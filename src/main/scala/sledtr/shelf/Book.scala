package sledtr.shelf
import sledtr._
import sledtr.util._
import scala.collection.mutable.Map
import java.io.{File}
import sledtr.MyPreDef._

class Book(val title: String, config: ConfigArray) {
  def workDir = "%s/%s".format(Environ.workHome, title.p)
  val chapters: Array[Chapter] = Chapter(this, config)
  def eachChapter(f: (Chapter) => Unit) = chapters.foreach { f }
  def eachSection(f: (Section) => Unit) = chapters.foreach { _.sections.foreach { f } }
  override def toString = title + chapters.foldLeft("") { (a, b) => " " + a + "\n " + b }
}

object Book {
  def apply(config: ConfigArray): Array[Book] =
    config.map { c => new Book(c.toMapValue.value("title"), c.toMapValue.arrayValue("chapters")) }
}