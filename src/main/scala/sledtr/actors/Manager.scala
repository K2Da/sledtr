package sledtr.actors
import scala.actors._, scala.actors.Actor._
import scala.collection.mutable._
import java.util.Date
import sledtr.MyPreDef._

class Manager(worker_count: Int , sleep_length: Long) extends Actor {
  val workers = Map[Worker, Int]()
  for(i <- List.range(0, worker_count)) workers += (new Worker() -> 0)
 
  val order_queue = new Queue[DoIt]
  val all_task: ListBuffer[Task] = ListBuffer()
 
  def waitReact(arr: Array[Task]) = {
    all_task ++= arr
    arr.foreach { t => this ! DoIt(t, this) }
    waitDone()
  }
  
  def addTask(task: Task) = {
    all_task += task
    this ! DoIt(task, this)
  }
  
  def waitDone() = {
    val sleep = if(sleep_length != 0) () => Thread.sleep(sleep_length) else () => Unit
    while(all_task.count { a => !a.isDone } != 0) sleep()
  }
  
  def resign() = this ! Exit()
     
  def act() = loop {
    react {
      case DoIt(ta, wm) =>
        order_queue.enqueue(DoIt(ta, wm))
        distribute()
       
      case Fin(ta: Task, w: Worker) =>
        workers(w) -= 1
        distribute()
       
      case Exit() =>
        workers.foreach { w => w._1 ! Exit() }
        exit
    }
  }
 
  def distribute(): Unit = {
//    
//    print(" R:" + order_queue.size + " ")
//    workers.foreach { pair => print(pair._2.toString) }
//    log("")
//   
    var free = workers.find { _._2 == 0 } match { case Some(w) => w._1 case None => return }
   
    if(order_queue.size == 0) return
    workers(free) += 1
    free ! order_queue.dequeue()
  }
 
  this.start()
}