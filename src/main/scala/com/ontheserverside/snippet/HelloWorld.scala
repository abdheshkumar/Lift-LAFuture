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
import com.sun.org.glassfish.external.statistics.annotations.Reset
import com.ontheserverside.lib.FutureWithJs
import net.liftweb.http.js.JE.JsRaw

class HelloWorld {
  def render = {
    "#scala-future *" #> Future {
      Thread.sleep(500); date
    } &
      "#lift-lafuture *" #> LAFuture.build {
        Thread.sleep(600); date
      } &
      "#btn" #> SHtml.ajaxSubmit("Click", () => {
        val result = FutureService.processBusinessLogic
        println("::::::::Result:::" + result.isCompleted)
        val js = FutureWithJs(result, JsRaw("alert('hello,done future')").cmd).cmd
        println(":::::Done:::")
        js
      })
  }

  private def date: String = {
    new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())
  }
}

