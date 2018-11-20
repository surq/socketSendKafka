package com.moxiu.util

/**
 * @author 宿荣全
 * date: 2018-11-19
 *  分发客户端socket实例
 */
class SocketDispatcher extends Runnable {
  override def run() =while (true)  SocketServer.executorsPool.submit(new SocketClient(SocketServer.socketQueue.take))
}