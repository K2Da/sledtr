package sledtr.formatter.filter
import net.htmlparser.jericho._
import scala.collection.JavaConversions._
import sledtr.MyPreDef._
import sledtr._

abstract class Tag {
  def replaceInline(elm: Element): String = {
    var c = elm.toString
    sun(elm)
    
    def sun(e: Element): Unit = {
      e.getName match {
        case "br"     => c = replaceTag(e, "<br />", c)
        case "li"     => c = replaceTag(e, "<li>%s</li>".format(e.getContent), c)
        case "strong" => c = replaceTag(e, "<strong>%s</strong>".format(e.getContent), c)
        case "script" =>
        case _    => c = deleteTag(e, c)
      }
      for(child <- e.getChildElements) sun(child)
    }
    
    c
  }
  
  def deleteTag(e: Element, s: String): String = {
    s.indexOf(e.toString) match {
      case -1 => """<.*?>""".r.replaceAllIn(s, "")
      case x  => s.substring(0, x) + e.getContent + s.substring(x + e.toString.length)
    }
  }
  
  def replaceTag(e: Element, r: String, s: String): String = {
    s.indexOf(e.toString) match {
      case -1 => log("okasiiii!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"); ""
      case x  => s.substring(0, x) + r + s.substring(x + e.toString.length)
    }
  }
}

object DefaultTag extends Tag {
  def minH(elm: Element): Int = {
    val l = elm.getAllElements map { _.getName } filter {
        s => s.startsWith("h") && s.length == 2 && s.substring(1).matches("""-?\d""")
      } map { _.substring(1).toInt }
    if(l.isEmpty) 0 else l min
  }
  
  def getHtml(url: String, elm: Element, sb: StringBuffer): Unit = {
    val minh = minH(elm)
    
    for(child <- elm.getChildElements()) {
      child.getName match {
        case "p"   => P.getHtml(child, sb)
        case "h1"  => H.getHtml(child, sb, minh)
        case "h2"  => H.getHtml(child, sb, minh)
        case "h3"  => H.getHtml(child, sb, minh)
        case "h4"  => H.getHtml(child, sb, minh)
        case "h5"  => H.getHtml(child, sb, minh)
        case "h6"  => H.getHtml(child, sb, minh)
        case "img" => IMG.getHtml(url, child, sb)
        case "ol"  => OL.getHtml(child, sb)
        case "ul"  => UL.getHtml(child, sb)
        case "pre" => PRE.getHtml(child, sb)
        case "a"   => A.getHtml(child, sb)
        case _   =>
          if(child.getName == "script")
            return
          else if (child.getContent.toString.trim.startsWith("<"))
            DefaultTag.getHtml(url, child, sb)
          else
            P.getHtml(child, sb)
      }
    }
  }
}

object UL extends Tag {
  def getHtml(elm: Element, sb: StringBuffer): Unit = {
    sb.append("\n<ul>%s</ul>".format(replaceInline(elm)))
  }
}
object PRE extends Tag {
  def getHtml(elm: Element, sb: StringBuffer): Unit = {
    sb.append("\n<pre>%s</pre>".format(elm.getContent()))
  }
}
object OL extends Tag {
  def getHtml(elm: Element, sb: StringBuffer): Unit = {
    sb.append("\n<ol>%s</ol>".format(replaceInline(elm)))
  }
}

object P extends Tag {
  def getHtml(elm: Element, sb: StringBuffer): Unit = {
    sb.append("\n<p>%s</p>".format(replaceInline(elm)))
  }
}

object A extends Tag {
  def getHtml(elm: Element, sb: StringBuffer): Unit = {
    sb.append("\n<p>%s</p>".format(replaceInline(elm)))
  }
}

object H extends Tag {
  def getHtml(elm: Element, sb: StringBuffer, minh: Int): Unit = {
    val l = elm.getName.substring(1).toInt
    val h = (l - minh + 3) min 6
    
    sb.append("\n<h%1$d>%2$s</h%1$d>".format(h, replaceInline(elm)))
  }
}
