package sledtr
import org.yaml.snakeyaml._
import scala.io._
import java.util._
import sledtr.actors._
import sledtr.MyPreDef._
import sledtr.util._
import sledtr.shelf._
import sledtr.config._

object App {
  var books: Array[Book] = _
  
  def main(args: Array[String]): Unit = {
    if(args.length != 1) {
      println("設定ファイルのパスを引数にする")
      exit
    }
    readConfig(args(0))
    startUp()
    
    try {
      books.foreach { _.eachChapter { _.readSource() } }
      HtmlDownloadManager.waitDone()
    
      books.foreach { _.eachChapter { _.createSection() } }
      books.foreach { _.eachSection { _.readSource() } }
      SectionDownloadManager.waitDone()
    
      books.foreach { MobiGenerator.generateBook }
    } catch {
      case e: Exception =>
        log(e.getMessage)
        e.printStackTrace
      case x => log(x)
    } finally cleanUp()
  }
 
  def readConfig(yamlpath: String): Unit = {
    val yaml = new Yaml()
    val obj = yaml.load(Source.fromFile(yamlpath, FileUtil.CEncoding).mkString)
    val c = ConfigValue(obj)

    books = Book(c.toMapValue.arrayValue("books"))
    Environ.init(c.toMapValue.mapValue("env"))
  }
  
  def startUp(): Unit = {
  }
  
  def cleanUp(): Unit = {
    HtmlDownloadManager.resign()
    SectionDownloadManager.resign()
    ImageDownloadManager.resign()
  }
}