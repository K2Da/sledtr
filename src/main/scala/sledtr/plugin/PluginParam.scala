package sledtr.plugin
import scala.collection.mutable.Map
import sledtr.MyPreDef._
import sledtr.shelf._

abstract class PluginParam()

case class StringParam(var value: Option[String])
  extends PluginParam()

case class ListParam(var value: List[String])
  extends PluginParam()

abstract class SelectedCompanion[T] {
  def getShortName: String = {
    getClass.getName.replace("$", "").substring(getClass.getPackage.getName.length + 1)
  }
}