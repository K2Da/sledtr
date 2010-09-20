package sledtr.formatter
import sledtr.formatter.sites._
import sledtr._
import sledtr.shelf._
import scala.collection.mutable._
import sledtr.MyPreDef._
import sledtr.plugin._

object Formatter extends Selector[FormatterCompanion, Formatter] {
  val list: List[FormatterCompanion] =
    List(
      SimpleExtender,
      IgnFormatter,
      SimpleFormatter,
      DefaultFormatter
    )
    
  def create(chapter: Chapter, section: Section, map: ConfigMap): Formatter = {
    this.select(map) match {
      case f: FormatterCompanion => f(chapter, section, map)
      case _ => throw new Throwable
    }
  }
}

abstract class Formatter(val chapter: Chapter,val section: Section, map: ConfigMap)
  extends Selected(map) {
  override def toString(): String = {
    val c = getClass
    c.getName.replace(c.getPackage.getName + ".", "")
  }
  
  def readSource(): Unit
  
  def formatHtml(): String
}