package sledtr.util
import sledtr.MyPreDef._

import java.io.{File, PrintWriter, FileOutputStream}
  
object FileUtil {
  val CEncoding = "UTF-8"

  def write(file: File, text: String): Unit = write(file.getAbsolutePath(), text)

  def write(filepath: String, text: String): Unit = {
    val file = new File(filepath)
    makeDir(file.getParentFile)
    
    val writer = new PrintWriter(file, CEncoding)
    
    try{ writer.write(text) }
    finally { writer.close() }
  }
  
  def write(barr: Array[Byte], file: File): Unit = {
    makeDir(file.getParentFile)
    val writer = new FileOutputStream(file)
    try{
      writer.write(barr)
    } finally {
      writer.close()
    }
  }

  def makeDir(f: File) {
    if(!f.getParentFile.exists) makeDir(f.getParentFile)
    else f.mkdirs()
  }
}