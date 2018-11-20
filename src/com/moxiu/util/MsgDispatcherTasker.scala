package com.moxiu.util

import scala.collection.mutable.ArrayBuffer

class MsgDispatcherTasker extends Runnable {
  override def run() {
        // 默认值：100条，1秒
    val size = SocketServer.properties.getProperty("kafka.message.split.size", "100").trim.toInt
    val interval = SocketServer.properties.getProperty("kafka.message.split.time", "1").trim.toInt
    var index = 0
    var sedList: ArrayBuffer[(String, String)] = null
    var intervalTime = System.currentTimeMillis + interval*1000
    while (true) {
      // 根据时间间隔和长度划分kafka message
      if (index % size == 0 || System.currentTimeMillis >= intervalTime) {
        index = 0
        if (sedList != null && sedList.size > 0) {
          SocketServer.executorsPool.submit(new MessageSentTasker(sedList.toList))
          intervalTime = System.currentTimeMillis + interval
        }
        sedList = ArrayBuffer[(String, String)]()
      }
      index = index + 1
      sedList += SocketServer.kaFkaMesgQueue.take
    }
  }
}