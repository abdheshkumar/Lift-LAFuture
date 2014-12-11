package com.ontheserverside.lib

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object FutureService {

  /**
   * Here we are doing server side processing
   */
  def processBusinessLogic: Future[String] = {
    Future {
      println(":::::::Run Scala Future::")
      Thread.sleep(2000)
      "Hello from scala future"
    }
  }
}