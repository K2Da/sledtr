package sledtr.util

import scala.io.Source
import org.mozilla.universalchardet.UniversalDetector 
import java.net._
import java.io.{InputStream}
import sledtr.MyPreDef._
import scala.collection.mutable._

object HttpReader {
  def fromURL(url: String): String = {
    val bhtml: Array[Byte] = getByteArray(url)
    val shtml: String = new String(bhtml, getEnc(bhtml))
    shtml
  }
  
  def fromFile(file: File): String = {
    val path = file.getAbsolutePath()
    Source.fromFile(path, FileUtil.CEncoding).mkString
  }
  
  def getEnc(ba: Array[Byte]): String = {
    val ud: UniversalDetector = new UniversalDetector(null)
    val buf: Array[Byte] = new Array[Byte](4096)
    var i: Int = 0
  
    while(i * buf.size <= ba.size && !ud.isDone()) {
      Array.copy(ba, i, buf, 0, buf.size)
      ud.handleData(buf, 0, buf.size)
      i = i + 1
    }
    ud.dataEnd()
    
    if(ud.getDetectedCharset == null) FileUtil.CEncoding else ud.getDetectedCharset
  }
  
  def getImage(url: String, file: File): Unit =
    if(!file.exists) FileUtil.write(getByteArray(url), file)
  
  def getByteArray(url: String): Array[Byte] = {
    val is: InputStream = ((new URL(url)).openConnection()).getInputStream()
    try {
      val bl: ListBuffer[Byte] = ListBuffer[Byte]()
      var b: Byte = 0
      var i: Int = 0
      i = is.read()
      
      while(i > -1) {
        b = i.toByte
        bl += b
        i = is.read()
      }
      return bl.toArray
    } finally is.close()
  }
}