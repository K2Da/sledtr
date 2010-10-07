package sledtr.util
import org.scalatest.FunSuite

class Sha1Test extends FunSuite {
  test("sha1の取得") {
    assert(new Sha1Digest("aiueo").p== new Sha1Digest("aiueo").p)
    assert(new Sha1Digest("aiueo").p != new Sha1Digest("aiueo1").p)
    println(new Sha1Digest("aiuoeo").p)
  }
}