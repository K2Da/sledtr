package sledtr.util
import java.security.MessageDigest
import java.io.FileInputStream

class Sha1Digest(str: String) {
  val digestString: String = {
    val md = MessageDigest.getInstance("SHA-1")
    md.update(str.getBytes)
    md.digest.foldLeft("") { (s, b) => s + "%02x".format(if(b < 0) b + 256 else b) }
  }
  
  lazy val p = digestString
}