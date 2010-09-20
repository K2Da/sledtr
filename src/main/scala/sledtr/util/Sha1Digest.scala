package sledtr.util
import java.security.MessageDigest
import java.io.FileInputStream

class Sha1Digest(str: String) {
  def digest2String(digest: Array[Byte]): String = {
    val sb = new StringBuffer
    digest.foreach { b =>
      var d = b.toInt
      if (d < 0) { d += 256 }
      if (d < 16) { sb.append("0") }
      sb.append(Integer.toString(d, 16))
    }
    sb.toString
  }
  
  def getStringDigest(): String = {
    val md: MessageDigest = MessageDigest.getInstance("MD5")
    val dat: Array[Byte] = str.getBytes()
    md.update(dat)
    return digest2String(md.digest)
  }
  
  lazy val p = getStringDigest
}