package sledtr.section.filter
import org.scalatest.FunSuite
import net.htmlparser.jericho._
import sledtr._
import scala.collection.JavaConversions._
import sledtr.util._

class TagTest extends FunSuite {
  test("Tag系のテスト") {
    Environ.workHome = """c:/work/tdwork"""
    val s = new Source("<img src=http://img.f.hatena.ne.jp/images/fotolife/K/K2Da/20050306/20050306221525.jpg />")
    val sb = new StringBuffer()
    IMG.getHtml("", s.getAllElements()(0), sb)
    
    val p = """<img src='.*' />"""
    assert(p.r.findFirstIn(sb.toString) match {
      case Some(s) => true
      case _ => false
    }, "exp:{%s} res:{%s}".format(p, sb.toString))
  }
  
  test("中身にタグ以外が入ってるものは<p>扱いするテスト") {
    val s = new Source("<div><div>a</div></div>")
    val sb = new StringBuffer()
    DefaultTag.getHtml("", s.getAllElements()(0), sb)
    expect("\n<p>a</p>") { sb.toString }
  }
}

class HLevelTest extends FunSuite {
  var sb = new StringBuffer()
  
  test("h1が最大だったら、h1がh3にそれ以外がずれるテスト") {
    val s = new Source("""
<div><h1>1</h1><h2>2</h2><h3>3</h3></div>
""")
    expect(1) { DefaultTag.minH(s.getAllElements()(0)) }
    expect("\n<h3>1</h3>\n<h4>2</h4>\n<h5>3</h5>") {
      DefaultTag.getHtml("", s.getAllElements()(0), sb)
      sb.toString
    }
  }
  
  test("h2が最大だったら、h2がh3にそれ以外がずれるテスト") {
    sb = new StringBuffer
    val s = new Source("""
<div><h2>1</h2><h3>2</h3><h4>3</h4></div>
""")
    expect(2) { DefaultTag.minH(s.getAllElements()(0)) }
    expect("\n<h3>1</h3>\n<h4>2</h4>\n<h5>3</h5>") {
      DefaultTag.getHtml("", s.getAllElements()(0), sb)
      sb.toString
    }
  }
  
  test("hがなかったらどうにもならないテスト") {
    sb = new StringBuffer
    val s = new Source("""
<div><p>1</p><p>2</p><p>3</p></div>
""")
    expect(0) { DefaultTag.minH(s.getAllElements()(0)) }
    expect("\n<p>1</p>\n<p>2</p>\n<p>3</p>") {
      DefaultTag.getHtml("", s.getAllElements()(0), sb)
      sb.toString
    }
    
  }
}