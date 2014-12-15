package com.ontheserverside.snippet
import java.text.SimpleDateFormat
import java.util.Date
import com.ontheserverside.lib.FutureBinds._
import net.liftweb.actor.LAFuture
import net.liftweb.util.Helpers._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import net.liftweb.http.SHtml
import net.liftweb.http.js.JsCmds
import com.ontheserverside.lib._

object HelloLAFuture {

}

class HelloLAFuture {
  import ScalaFutureToLaFuture._
  def render = {
    "#btnLA" #> SHtml.ajaxSubmit("Click LAFuture", () => {
      val result: LAFuture[String] = FutureService.processBusinessLogic
      FutureWithJs(result, {
        JsCmds.Run("alert('Hello,future done')")
      }).cmd
    })
  }

  private def date: String = {
    new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())
  }
}
