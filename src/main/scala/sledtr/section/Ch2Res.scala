package sledtr.section
import java.util.{ Date, Calendar, GregorianCalendar }
import java.text.SimpleDateFormat

object Ch2Res {
  def apply(thread: String, no: Int, line: String): Option[Ch2Res] = {
    val RegRes = """(.*)<>(.*)<>(.*) ID:(.*)<> (.*)<>.*""".r
    if(RegRes.findFirstIn(line) == None) return None
    var RegRes(name, nazo, strdate, id, text) = line
    
    val RegDate = """(\d+)/(\d+)/(\d+)\(.\) (\d+):(\d+):(\d+)""".r
    if(RegDate.findFirstIn(strdate) == None) return None
    val RegDate(year, month, day, hour, minute, second) = strdate
    
    val date = new GregorianCalendar(
        year.toInt, month.toInt - 1, day.toInt,
        hour.toInt, minute.toInt, second.toInt)
    
    val RegTag = """<.*?>""".r
    name = RegTag.replaceAllIn(name, "")
    Some(new Ch2Res(thread, no, name.trim, id.trim, date.getTime, text.trim))
  }
}
class Ch2Res(
    val thread: String, val no: Int, val name: String,
    val id: String, val date: Date, val text: String) {
  
  def inNhour(n: Int): Boolean = {
    val calendar = new GregorianCalendar
    calendar.setTime(new Date)
    calendar.add(Calendar.HOUR, -n)
    date after calendar.getTime
  }
  
  def isNG(ngword: List[String]) = {
    ngword.exists { word => text.contains(word) || name.contains(word) }
  }
  
  def format(): String = {
    var f = new SimpleDateFormat("MM/dd kk:mm:ss")
"""
<p>
<br />
%d <b>%s</b> %s ID:%s<br />
%s
<br />
</p>
""".format(no, name, f.format(date), id, text)
  }
}