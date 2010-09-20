import sbt._
import de.element34.sbteclipsify._

class MySbtProject(info: ProjectInfo) extends DefaultProject(info)
  with Eclipsify with ProguardProject {
  val scalaTest = "org.scalatest" % "scalatest" % "1.2"
  val jerichoHtmlParser = "net.htmlparser.jericho" % "jericho-html" % "3.1"
  def keepMainClass = """
-keepclasseswithmembers public class *{ public static void main(java.lang.String[]);}"""
    
  override def proguardDefaultArgs = "-dontwarn" :: "-dontoptimize" :: "-dontobfuscate" :: keepMainClass :: proguardOptions
  override def compileOptions = super.compileOptions ++ Seq(Unchecked)
}
