package sledtr.source
import org.scalatest.FunSuite

class SourceTest extends FunSuite {
  test("get short name") {
    assert(RssFeed.getShortName == "RssFeed")
  }
}