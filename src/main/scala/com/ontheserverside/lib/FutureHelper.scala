package com.ontheserverside.lib

import net.liftweb.actor._
import net.liftweb.common._
import net.liftweb.http._
import net.liftweb.http.js._
import net.liftweb.util.CanBind
import net.liftweb.http.js.JsCmds._
import net.liftweb.util.Helpers._
import xml.{ Node, Elem, NodeSeq }
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object ScalaFutureToLaFuture {

  implicit def scalaFutureToLaFuture[T](scf: Future[T])(implicit m: Manifest[T]): LAFuture[T] = {
    val laf = new LAFuture[T]
    scf.onSuccess {
      case v: T => laf.satisfy(v)
      case _ => laf.abort
    }
    scf.onFailure { case e: Throwable => laf.fail(Failure(e.getMessage(), Full(e), Empty)) }
    laf
  }
}

case class FutureWithJs[T](future: LAFuture[T], js: JsCmd) extends JsCmd with Loggable {

  val futureCompleted_? = (f: LAFuture[T]) => f.isSatisfied

  lazy val updateFunc = SHtml.ajaxInvoke(() => { resolveAndUpdate }).exp.cmd

  def resolveAndUpdate: JsCmd =
    if (futureCompleted_?(future)) js
    else After(1 seconds, updateFunc)

  override val toJsCmd = updateFunc.toJsCmd
  val cmd = updateFunc
}