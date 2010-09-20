package sledtr.plugin
import sledtr.MyPreDef._
import sledtr.config._
import sledtr.shelf._
import scala.collection.mutable.Map

abstract class Selector[C <: SelectedCompanion[T], T] {
  val list: List[C]

  def select(map: ConfigMap): SelectedCompanion[T] = {
    val selected_type = map("type") match {
      case Value(v) => v
      case _ => throw new Throwable()
    }
   
    list.foreach { f => f.getShortName }
    val companion = list.find(f => f.getShortName == selected_type)
    companion match {
      case Some(c) => c
      case None => throw new Throwable("no hit! selected type:[%s]".format(selected_type))
    }
  }
}

abstract class Selected(map: ConfigMap) {
  val params: Map[String, PluginParam]
 
  def initParams() = {
    map.foreach { d =>
      val key = d._1
      if(params.contains(key)) {
        d._2 match {
          case Value(s) => params(key) match {
            case StringParam(d) => params(key) = StringParam(Some(s))
            case ListParam(l) => params(key) = ListParam(s :: l)
            case _ => throw new Throwable("")
          }
        }
      }
    }
  }
  
  override def toString(): String = {
    val c = getClass
    c.getName.replace(c.getPackage.getName + ".", "") + params
  }
}
