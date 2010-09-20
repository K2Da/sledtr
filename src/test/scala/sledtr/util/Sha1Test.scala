package sledtr.util
import org.scalatest.FunSuite

class Sha1Test extends FunSuite {
  test("sha1の取得") {
    assert(new Sha1Digest("aiueo").getStringDigest == new Sha1Digest("aiueo").getStringDigest)
    assert(new Sha1Digest("aiueo").getStringDigest != new Sha1Digest("aiueo1").getStringDigest)
    println(new Sha1Digest("aiuoeo").getStringDigest)
  }
}