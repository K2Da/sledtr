package sledtr.actors
import org.scalatest.FunSuite

class ActorsTest extends FunSuite {
  test("Actorのテスト") {
    val mng = new Manager(3, 500)
    mng.addTask(new t("a"))
    mng.waitDone()
    assert(true)
    mng.addTask(new t("b"))
    mng.waitDone()
    assert(true)
  }
}
class t(s: String) extends Task() {
  override def myTask(): Unit = { }
  
  override def debugInfo(): String = s
}