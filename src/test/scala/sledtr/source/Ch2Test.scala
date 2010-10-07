package sledtr.source
import org.scalatest.FunSuite
import sledtr.MyPreDef._
import sledtr.config._
import scala.collection.mutable.Map

class Ch2Test extends FunSuite {
  test("source.txtからurlとタイトルを生成する") {
    val p: ConfigMap = Map(
        "url" -> ConfigValue("http://yuzuru.2ch.net/gamefight/"),
        "keyword" -> ConfigValue("ウメスレ")
      )
    val ch2: Ch2 = Ch2(null, p) match { case x: Ch2 => x }
    
    val (title, url) = ch2.getTitleUrl("1285113690.dat<>ドラゴンボール(x) (107)")
    
    expect("ドラゴンボール(x)") { title }
    expect("http://yuzuru.2ch.net/gamefight/dat/1285113690.dat") { url }
  }
}