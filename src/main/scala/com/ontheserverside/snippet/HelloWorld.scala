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
import com.ontheserverside.lib.FutureService
import com.ontheserverside.lib.FutureHelper
import com.ontheserverside.lib.FutureHelper.FutureIsHereWithJsCmd

class HelloWorld {
  import FutureHelper.ScalaFutureToLaFuture._
  def render = {
    "#scala-future *" #> Future { Thread.sleep(500); date } &
      "#lift-lafuture *" #> LAFuture.build { Thread.sleep(600); date } &
      "#btn" #> SHtml.ajaxButton("Click", () => {
        val result: LAFuture[String] = FutureService.processBusinessLogic
        FutureIsHereWithJsCmd(result, {
          JsCmds.Run("alert('Hello,future done')")
        }).cmd
      })
  }

  private def date: String = {
    new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())
  }
}

