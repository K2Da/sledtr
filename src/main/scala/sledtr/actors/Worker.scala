package sledtr.actors
import scala.actors._, scala.actors.Actor._
import scala.collection.mutable._
import java.util.Date
import sledtr.MyPreDef._

case class DoIt(ta: Task, wm: Manager)
case class Fin(ta: Task, w: Worker)
case class Exit()


// TODO 同じドメインのリクエストが近すぎたら、managerに送り返す
// TODO もしくは現在実行中の中に同じドメインのがあったらキューの最後に戻す

class Worker() extends Actor {
  def act() = loop {
    react {
      case DoIt(ta: Task, wm: Manager) =>
        ta.doIt()
        wm ! Fin(ta, this)
      case Exit() => exit
    }
  }
 
  this.start()
}