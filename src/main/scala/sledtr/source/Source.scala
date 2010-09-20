package sledtr.source
import sledtr.shelf._
import sledtr._
import scala.collection.mutable._
import sledtr.MyPreDef._
import sledtr.plugin._

object Source extends Selector[SourceCompanion, Source] {
  val list: List[SourceCompanion] = Collections.SourceList
    
  def create(chapter: Chapter, map: ConfigMap): Source = {
    this.select(map) match {
      case s: SourceCompanion => s(chapter, map)
      case _ => throw new Throwable
    }
  }
}

abstract class Source(chapter: Chapter, map: ConfigMap) extends Selected(map) {
  val sections = ListBuffer[Section]()
  
  def readSource(): Unit
  def createSection(): Unit
}