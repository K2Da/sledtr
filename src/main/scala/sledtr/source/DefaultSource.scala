package sledtr.source
import sledtr._
import sledtr.shelf._
import sledtr.MyPreDef._
import scala.collection.mutable.Map
import sledtr.plugin._

object DefaultSource extends SourceCompanion {
  def apply(chapter: Chapter, map: ConfigMap): Source = {
    new DefaultSource(chapter, map)
  }
}
class DefaultSource private(chapter: Chapter, map: ConfigMap) extends Source(chapter, map) {
  val params: Map[String, PluginParam] = Map()
  
  override def readSource(): Unit = { }
  override def createSection(): Unit = { }
}