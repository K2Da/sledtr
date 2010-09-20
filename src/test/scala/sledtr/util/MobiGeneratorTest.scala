package sledtr.util
import org.scalatest.FunSuite

class MobiGeneratorTest extends FunSuite {
  test("Opfファイルのテスト") {
    val r = MobiGenerator.createOpf("title", "source.html")
    println(r)
    assert(r.length != 0)
  }

  test("Ncxのテスト") {
    val r = MobiGenerator.createToc("title", "source.html")
    println(r)
    assert(r.length != 0)
  }
}