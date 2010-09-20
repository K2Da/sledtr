package sledtr.util
import sledtr.MyPreDef._
import sledtr.shelf._
import sledtr._
import sledtr.actors._

abstract class MobiOutput {
  val sb = new StringBuffer
  def contents: String = sb.toString
  
  def start(book: Book) = { }
  def startChapter(c: Chapter, n: Int) = { }
  def inSection(s: Section, n: Int, m: Int) = { }
  def endChapter() = { }
  def end() = { }
}

class OutputHtml extends MobiOutput {
  override def startChapter(c: Chapter, n: Int) = {
    sb.a("<h2 id=%s>%d. %s</h2>".format(c.title.p, n + 1, c.title))
    sb.a("<mbp:pagebreak />")
  }
  
  override def inSection(s: Section, n: Int, m: Int) = {
    sb.a("<h2 id=%s>%d-%d. %s</h3>".format(s.url.p, n + 1, m + 1, s.title))
    sb.a("<p>%s</p>".format(s.url))
    sb.a(s.formatHtml)
    sb.a("<mbp:pagebreak />")
  }
}

class HtmlToc extends MobiOutput {
  override def start(book: Book) = {
    sb.a("<h1>%s</h1>".format(book.title))
    sb.a("<mbp:pagebreak />")
    sb.a("<h2 id=toc>Contents</h2>")
    sb.a("<ul>")
  }
  
  override def startChapter(c: Chapter, n: Int) = {
    sb.a("<li><a href=#%s>%d. %s</a></li>".format(c.title.p, n + 1, c.title))
    sb.a("<ul>")
  }
  
  override def inSection(s: Section, n: Int, m: Int) =
    sb.a("<li><a href=#%s>%d-%d. %s</a></li>".format(s.url.p, n + 1, m + 1, s.title))
  
  override def endChapter() = sb.a("</ul>")
  
  override def end() = {
    sb.a("</ul>")
    sb.a("<mbp:pagebreak />")
  }
}

class Ncx extends MobiOutput {
  override def startChapter(c: Chapter, n: Int) =
    sb.a(MobiGenerator.createNavi(c.title, c.title.p))
  
  override def inSection(s: Section, n: Int, m: Int) =
    sb.a(MobiGenerator.createNavi(s.title, s.url.p))
}

class CheckText extends MobiOutput {
  override def inSection(s: Section, n: Int, m: Int) = {
    sb.a("===== " + s.title + " =====")
    sb.a(s.url)
    import sledtr.formatter._
    s.formatter match {
      case f: SimpleFormatter =>
        sb.a(filter.HtmlInfo.check(f.src, f.findMaybeTarget))
      case _ =>
    }
  }
}

object MobiGenerator {
  val opf_name  = "book.opf"
  val ncx_name  = "toc.ncx"
  val chk_name  = "check.txt"
  val con_name  = "contents.html"
    
  def generateBook(book: Book) = {
    val outputs = List(new OutputHtml, new HtmlToc, new Ncx, new CheckText) 
    val con :: toc :: ncx :: chk :: Nil = outputs
    
    outputs.foreach { _.start(book) }
    
    book.chapters.zipWithIndex.foreach { case (c, n) =>
      outputs.foreach { _.startChapter(c, n) }
      
      c.sections.zipWithIndex.foreach { case(s, m) => outputs.foreach { _.inSection(s, n, m) } }
      
      outputs.foreach { _.endChapter() }
    }
    outputs.foreach { _.end() }
    
    FileUtil.write(book.workDir + "/" + opf_name, createOpf(book.title, con_name))
    FileUtil.write(book.workDir + "/" + ncx_name, createToc(book.title, ncx.contents))
    FileUtil.write(book.workDir + "/" + chk_name, chk.contents)
    
    ImageDownloadManager.waitDone()
    generate(book, toc.contents + con.contents)
  }
  
  def generate(book: Book, body: String): Unit = {
    FileUtil.write(book.workDir + "/" + con_name, formatHtml(book.title, body))
  
    val ret = ExeExecutor.exec("%s %s".format(Environ.kindleGenPath, book.workDir + "\\" + opf_name))
    for(s <- ret) log(s)
  }
  
  def formatHtml(title: String, body: String): String = {
"""
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>%s</title>
  </head>
  <body>
%s
  </body>
</html>
""".format(title, body)
  }
  
  def createOpf(title: String, html_name: String): String = {
    import java.util.Calendar
    val c = Calendar.getInstance()
    val date = "%1$02d/%2$02d/%3$d".format(c.get(Calendar.DATE), c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR))
"""
<?xml version="1.0" encoding="utf-8"?>
<package unique-identifier="uid">
  <metadata>
    <dc-metadata
      xmlns:dc="http://purl.org/metadata/dublin_core"
      xmlns:oebpackage="http://openebook.org/namespaces/oeb-package/1.0/">
      <dc:Title>%1$s</dc:Title>
      <dc:Language>en-us</dc:Language>
      <dc:Creator>Internet</dc:Creator>
      <dc:Description>from the Internet</dc:Description>
      <dc:Date>%2$s</dc:Date>
    </dc-metadata>
    <x-metadata>
      <output encoding="utf-8" content-type="text/x-oeb1-document"></output>
    </x-metadata>
  </metadata>
  <manifest>
    <item id="item1" media-type="text/x-oeb1-document" href="%3$s"></item>
    <item id="toc" media-type="application/x-dtbncx+xml" href="toc.ncx"></item>
  </manifest>
  <spine toc="toc">
    <itemref idref="item1" />
  </spine>
  <tours></tours>
  <guide>
    <reference type="toc" title="Table of Contents" href="%3$s#toc"></reference>
    <reference type="start" title="Startup Page" href="%3$s"></reference>
  </guide>
</package>
""".format(title, date, html_name)
  }
  
  def createNavi(label: String, content: String): String = {
"""
    <navPoint id="titlepage" playOrder="1">
      <navLabel><text>%1$s</text></navLabel>
      <content src="%2$s#%3$s"/>
    </navPoint> 
""".format(label, con_name, content)
  }
  
  def createToc(title: String, nav: String): String = {
"""
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE ncx PUBLIC "-//NISO//DTD ncx 2005-1//EN" 
                     "http://www.daisy.org/z3986/2005/ncx-2005-1.dtd">
<ncx xmlns="http://www.daisy.org/z3986/2005/ncx/" version="2005-1" xml:lang="en-US">
  <head>
    <meta name="dtb:uid"            content="uid"/>
    <meta name="dtb:depth"          content="1"/>
    <meta name="dtb:totalPageCount" content="0"/>
    <meta name="dtb:maxPageNumber"  content="0"/>
  </head>
    <docTitle><text>%1$s</text></docTitle>
    <docAuthor><text>Internet</text></docAuthor>
  <navMap>
%2$s
  </navMap>
</ncx>    
""".format(title, nav)
  }
}
