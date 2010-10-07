package sledtr.section
import org.scalatest.FunSuite
import java.text.SimpleDateFormat

class Ch2SectionTest extends FunSuite {
  test("レスをバラバラにする") {
    var m = Ch2Res("", 0, "俺より強い名無しに会いにいく<><>2010/09/25(土) 20:25:03 ID:G3NorCXq0<> おいｗ<>")
    var f = new SimpleDateFormat("yyyy/MM/dd kk:mm:ss")
    (m: @unchecked) match {
      case Some(r) =>
        expect("俺より強い名無しに会いにいく") { r.name }
        expect("G3NorCXq0") { r.id }
        expect("2010/09/25 20:25:03") { f.format(r.date) }
        expect("おいｗ") { r.text }
    }
    
    m = Ch2Res("", 0, "俺より強い名無しに会いにいく<>sage<>2010/09/25(土) 20:34:34 ID:3a/O5zYI0<> ネモ明日十一試合かよｗ <> ")
    (m: @unchecked) match {
      case Some(r) =>     
        expect("俺より強い名無しに会いにいく") { r.name }
        expect("3a/O5zYI0") { r.id }
        expect("2010/09/25 20:34:34") { f.format(r.date) }
        expect("ネモ明日十一試合かよｗ") { r.text }
    }
  }
}
