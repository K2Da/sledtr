package sledtr
import sledtr.MyPreDef._
import java.io._

object Environ {
  def init(config: ConfigMap) = {
    println(config.value("workdir"))
    workHome = config.value("workdir")
    println(config.value("kindlegen"))
    kindleGenPath = config.value("kindlegen")
  }
  var workHome      : String= _
  var kindleGenPath : String= _
  
  def imageDir = workHome + "/images"
  def srcDir = workHome + "/src"
} 