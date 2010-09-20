package sledtr.util
import java.lang._
import java.io._
import sledtr.MyPreDef._

object ExeExecutor {
  def exec(command:String): List[String] = { 
    def getResult(command:String): Option[BufferedReader] = {
      try {
        val is = Runtime.getRuntime.exec(command).getInputStream
        Some(new BufferedReader(new InputStreamReader(is)))
      } catch {
        case x =>
          log(x.toString)
          x.printStackTrace
          None
      }
    }

    def read(br:BufferedReader, list:List[String]):List[String] = br.readLine match {
      case null => list
      case s => read(br, s::list)
    }

    getResult(command) match {
      case Some(br) => read(br, Nil).reverse
      case _ => Nil
    }
  }
}