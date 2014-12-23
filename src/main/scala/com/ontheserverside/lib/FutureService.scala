package com.ontheserverside.lib

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import net.liftweb.http.SHtml
import net.liftweb.http.js.JsCmds
import net.liftweb.actor.LAFuture
import scala.xml.NodeSeq
import net.liftweb.common.Box

object FutureService {
  var lists = List[String]("Hello, Lifters")
  /**
   * Here we are doing server side processing
   */
  def processBusinessLogic: Future[String] = {
    Future {
      Thread.sleep(6000)
      "Hello from scala future"
    }
  }

  def buttonAction(la: LAFuture[NodeSeq]) = {
    println("buttonAction was called")
    Thread.sleep(3000L)
    val ns = <span class="alert alert-success">{ Thread.currentThread().getName }</span>
    la.satisfy(ns)
  }

  def getList: List[String] = lists

  /**
   * Maybe this function take little time to complete business logic
   * for moment i just use sleep funciton for testing purpose
   */
  def saveItem(item: Box[String]): Future[Box[String]] = Future {
    Thread.sleep(1000L)
    item.filterNot(_.isEmpty()).map {
      f =>
        lists +:= f
        f
    }

  }
}