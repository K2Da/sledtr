package sledtr.formatter.filter
import net.htmlparser.jericho._
import scala.collection.JavaConversions._
import sledtr.MyPreDef._

// 調査用にHTMLの構造を書き出す
object HtmlInfo {
  def check(src: Source, target: Element): String = {
    val sb = new StringBuffer
    for(elm <- src.getChildElements) r(elm, target, sb, 0)
    sb.toString
  }
  
  def r(elm: Element, target: Element, sb: StringBuffer, depth: Int): Unit = {
    for(child <- elm.getChildElements()) {
      child.getName match {
        case "div" =>
          val id = child.getAttributeValue("id")
          val cls = child.getAttributeValue("class")
          if(id != null || cls != null) {
            sb.a(" " * depth * 2 + "<div%s%s>%s".format(
                if(id != null) " id=" + id else "",
                if(cls != null) " class=" + cls else "",
                if(child == target) " < ============ target" else ""))
            r(child, target, sb, depth + 1)
            sb.a(" " * depth * 2 + "</div>")
          } else r(child, target, sb, depth + 1)
          
        case _ => r(child, target, sb, depth)
      }
    }
  }
}