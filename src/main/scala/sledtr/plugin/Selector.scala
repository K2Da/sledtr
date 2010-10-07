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
    map.foreach { case(key, value) =>
      if(params.contains(key)) {
        value match {
          case Value(s) => params(key) match {
            case StringParam(d) => params(key) = StringParam(Some(s))
            case ListParam(l)   => params(key) = ListParam(s :: l)
            case _ => throw new Throwable("inner")
          }
          
          case ArrayValue(a) =>
            params(key) = ListParam(a.toList.map {
                v => v match {
                  case Value(s) => s
                  case _ => throw new Throwable("nest parameter")
                }
            } )
            
          case x: ConfigValue => throw new Throwable("outer : " + x.toString(0))
        }
      }
    }
  }
  
  def getStringParam(name: String): String = {
    params(name) match {
      case StringParam(Some(s)) => s
      case _ => throw new Throwable("prameter keyword is not found")
    }
  }
  
  def getListParam(name: String): List[String] = {
    params(name) match {
      case ListParam(s) => s
      case _ => throw new Throwable("prameter keyword is not found")
    }
  }
  
  override def toString(): String = {
    val c = getClass
    c.getName.replace(c.getPackage.getName + ".", "") + params
  }
}
