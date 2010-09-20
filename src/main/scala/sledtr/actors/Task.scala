package sledtr.actors
import java.util.Date
import sledtr.MyPreDef._

abstract class Task() {
  var start, finish: Date = _
 
  def doIt() = {
    start = new Date()
    try { myTask() }
    catch {
      case e =>
        log(this.debugInfo)
        log(e)
        e.printStackTrace()
    }
    finish = new Date()
  }
 
  def isDone = finish != null
 
  protected def debugInfo(): String
  protected def myTask(): Unit
}