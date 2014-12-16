package com.ontheserverside.snippet
import java.text.SimpleDateFormat
import com.ontheserverside.lib.FutureService._
import java.util.Date
import net.liftweb.actor.LAFuture
import net.liftweb.util.Helpers._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import net.liftweb.http.SHtml
import net.liftweb.http.js.JsCmds
import com.ontheserverside.lib._
import net.liftweb.actor.LAScheduler
import scala.xml.NodeSeq
import com.ontheserverside.lib.LiftHelper._

class HelloLAFuture {

  val f1: LAFuture[NodeSeq] = new LAFuture()
  LAScheduler.execute(() => buttonAction(f1))

  def render = {
    "#btnLA" #> f1
  }

  private def date: String = {
    new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())
  }
}
