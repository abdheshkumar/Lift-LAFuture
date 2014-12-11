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

case class FutureWithJs[T](future: Future[T], js: JsCmd) extends JsCmd with Loggable {

  val futureCompleted_? = (f: Future[T]) => f.isCompleted

  lazy val updateFunc = SHtml.ajaxInvoke(() => {
    println(":::updateFunc:::::::::")
    resolveAndUpdate
  }).exp.cmd

  def resolveAndUpdate: JsCmd = {
    if (futureCompleted_?(future)) {
      println(":::::::resolveAndUpdate Scala Future:::futureCompleted_?(concreteFuture):" + futureCompleted_?(future))
      js
    } else {
      println("TRY AGAIN::")
      After(1 seconds, updateFunc)
    }

  }

  override val toJsCmd = updateFunc.toJsCmd
  val cmd = updateFunc
}