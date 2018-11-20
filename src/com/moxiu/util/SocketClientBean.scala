package com.moxiu.util

import scala.beans.BeanProperty
import java.net.Socket

class SocketClientBean {
  @BeanProperty
  var socket: Socket = null
  @BeanProperty
  var hostName = ""
  @BeanProperty
  var hostIp = ""
  @BeanProperty
  var workInfo = ""
  @BeanProperty
  var port = 0
}