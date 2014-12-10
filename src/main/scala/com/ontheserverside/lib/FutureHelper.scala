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

object FutureHelper {

  object ScalaFutureToLaFuture {

    implicit def scalaFutureToLaFuture[T](scf: Future[T])(implicit m: Manifest[T]): LAFuture[T] = {
      val laf = new LAFuture[T]
      scf.onSuccess {
        case v: T =>
          println("::::::::ScalaFutureToLaFuture::::::::Future Completed::::" + v)
          laf.satisfy(v)
        case _ => laf.abort
      }
      scf.onFailure {
        case e: Throwable => laf.fail(Failure(e.getMessage(), Full(e), Empty))
      }
      println("::::::::ScalaFutureToLaFuture Ending::::::")
      laf
    }
  }

  case class FutureIsHereWithJsCmd[T](la: LAFuture[T], js: JsCmd) extends JsCmd with Loggable {

    val updatePage: JsCmd = if (la.isSatisfied) {
      println("::::::::FutureIsHereWithJsCmd::::::::Future Completed::::")
      js
    } else {
      println("::::::::FutureIsHereWithJsCmd::::::::updatePage else:::")
      tryAgain()
    }

    private def tryAgain(): JsCmd = {
      val funcName: String = S.request.flatMap(_._params.toList.headOption.map(_._1)).openOr("")
      val retry = "setTimeout(function(){liftAjax.lift_ajaxHandler('%s=true', null, null, null)}, 3000)".format(funcName)
      JE.JsRaw(retry).cmd
    }

    override val toJsCmd = updatePage.toJsCmd
    val cmd = updatePage
  }
}