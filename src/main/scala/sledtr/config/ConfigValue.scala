package sledtr.config
import sledtr.MyPreDef._
import org.yaml.snakeyaml._
import java.util._
import scala.collection.JavaConversions._
import scala.collection.mutable.Map

object ConfigValue {
  def apply(obj: Any): ConfigValue = {
    if(obj.isInstanceOf[LinkedHashMap[_, _]]) {
      val lhm = obj.asInstanceOf[LinkedHashMap[_, _]]
      val map = Map[String, ConfigValue]()
      lhm.foreach { t => map.put(t._1.toString(), ConfigValue(t._2)) }
      
      return MapValue(map)
    }
    
    if(obj.isInstanceOf[ArrayList[_]]) {
      val al = obj.asInstanceOf[ArrayList[AnyRef]]
      val arr: ConfigArray = new ConfigArray(al.size)
      for(i <- 0 to al.size() - 1) { arr(i) = ConfigValue(al(i)) }
      
      return ArrayValue(arr)
    }
    
    if(obj.isInstanceOf[String]) return Value(obj.asInstanceOf[String])
    
    return null
  }
}

abstract class ConfigValue {
  def toString(n: Int): String
  protected def B(n: Int): String = " " * n
  
  def toArrayValue(): ConfigArray =
    this match { case ArrayValue(arr) => arr case _ => throw new Throwable("a") }
  
  def toMapValue(): ConfigMap =
    this match { case MapValue(map) => map case _ => throw new Throwable("b") }
}

case class ArrayValue(value: ConfigArray) extends ConfigValue {
  def toString(n: Int): String = {
    value.map(_.toString(n + 1)).reduceRight { (x, y) => x + y} 
  }
}
case class Value(value: String) extends ConfigValue {
  def toString(n: Int): String = {
    " : %s\n".format(value)
  }
}
case class MapValue(value: ConfigMap) extends ConfigValue {
  def toString(n: Int): String = {
    val kv = value.map ( x => {
        val v = x._2 match {
          case MapValue(_)   => "\n" + x._2.toString(n + 3)
          case Value(_)      => ""   + x._2.toString(0)
          case ArrayValue(_) => "\n" + x._2.toString(n + 3)
        }
        B(n + 2) + x._1.toString() + v 
      }
    ).reduceRight { (x, y) => x + y }
  
    return kv
  }
}
// implicit
class ConfigGetter(map: ConfigMap) {
  def value(key: String): String =
    map(key) match { case Value(v) => v case _ => throw new Throwable(key + "ないよ") }
  
  def mapValue(key: String): ConfigMap =
    map(key) match { case MapValue(map) => map case _ => throw new Throwable(key + "ないよ") }
  
  def arrayValue(key: String): ConfigArray =
   map(key) match { case ArrayValue(arr) => arr case _ => throw new Throwable(key + "ないよ") }
}