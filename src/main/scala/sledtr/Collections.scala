package sledtr
import scala.collection.mutable._
import scala.util.matching._
import sledtr.source._
import sledtr.section._
import sledtr.section.sites._
import net.htmlparser.jericho._

object Collections {
  val SimpleExtenderSites: List[Tuple2[Regex, (Element) => Boolean]] = List(
      ( // はてな匿名ダイアリー
      	"http://anond.hatelabo.jp/.*".r,
        e => e.getName == "div" && e.getAttributeValue("class") == "day"
      )
      , 
      ( // はてなダイアリー
        "http://d.hatena.ne.jp/.*?/.*?".r,
        e => e.getName == "div" && e.getAttributeValue("class") == "body"
      )
    )
      
  val SourceList: List[SourceCompanion] = 
    List(
      Ch2,
      UrlList,
      DefaultSource,
      RssFeed
    )
  
  val SectionList: List[SectionCompanion] = 
    List(
      Ch2Section,
      SimpleExtender,
      IgnSection,
      SimpleSection,
      DefaultSection
    )
    
  val DivNames: List[String] =
    List(
        "CONTENTS_MAIN",
        "article-body entry-content",
        "entry_body",
        "articleTextnews1",
        "main_cont",
        "blogbody",
        "NewsArticle",
        "maincol"
    )
}