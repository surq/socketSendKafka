package com.moxiu.util

import java.net.Socket
import java.io.InputStreamReader
import java.io.BufferedReader
import java.io.PrintStream

/**
 * @author 宿荣全
 * @date 2018-11-19
 * socket 服务端对接客户端获取客户端信息
 */
class SocketClient(client: Socket) extends Runnable {
  override def run() {
    val response_msg = SocketServer.properties.getProperty("http.response.message", "").trim
    val reader = new BufferedReader(new InputStreamReader(client.getInputStream))
    val writer = new PrintStream(client.getOutputStream(), true)
    // 只取第一行，[GET /1111111111111111?user=123&types=aa HTTP/1.1]
    val first_line = reader.readLine()
    val resource = (first_line.substring(first_line.indexOf('/') + 1, first_line.lastIndexOf('/') - 5)).trim
    // 根据 HTTP 协议, 空行将结束头信息所以后面要加“\n”表示结束
    writer.println("HTTP/1.0 200 OK \n\n")
    writer.println(response_msg)
    editDate(resource)
    reader.close()
    writer.close()
    client.close()
  }

  /**
   * http会3次请求建立联接
   * 线程ID11:/1111111111111111?user=123&types=aaaaa
   * 线程ID12:/favicon.ico
   * 过滤掉请求头，只要请求体
   */
  def editDate(resource: String) = {
    val index = resource.indexOf('?')
    if (index > 0) {
      val topic = resource.substring(0, index)
      val msg = resource.substring(index + 1)
      SocketServer.kaFkaMesgQueue.offer((topic, msg))
    }
  }
}