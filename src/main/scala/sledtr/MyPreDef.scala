package sledtr
import sledtr.util._
import sledtr.config._
import sledtr.shelf._
import sledtr.section._
import scala.collection.mutable.Map

object MyPreDef {
  type File = java.io.File
  type ConfigMap = Map[String, ConfigValue]
  type ConfigArray = Array[ConfigValue]
  type SectionList = scala.collection.mutable.ListBuffer[Section]
  
  def log(info: Any): Unit = {
    Logger.log(info)
  }
  
  implicit def map2Config(map: ConfigMap) = new ConfigGetter(map)
  implicit def string2Sha1(s: String) = new Sha1Digest(s)
  implicit def Sb2LF(sb: StringBuffer): LF = new LF(sb)
}

class LF(sb: StringBuffer) { def a(s: String) = sb.append(s + "\n") }