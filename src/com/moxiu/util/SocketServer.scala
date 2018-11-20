package com.moxiu.util

import java.io.FileInputStream
//import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket
import java.util.Properties
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue

object SocketServer {
  val socketQueue = new LinkedBlockingQueue[Socket]()
  val kaFkaMesgQueue = new LinkedBlockingQueue[(String, String)]()
  val executorsPool = Executors.newCachedThreadPool()
  // 用户配置属性文件
  val properties = loadProperties

  def main(args: Array[String]): Unit = {
    val serverPort = properties.getProperty("socket.server.port").trim.toInt
    // 启动socket客户端分发线程--》向kaFkaMesgQueue注入msg
    executorsPool.submit(new SocketDispatcher)
    // 启动kafka消息分隔包线程
    executorsPool.submit(new MsgDispatcherTasker)

    //配置文件获取
    val serverSocket = new ServerSocket(serverPort)
    Console println "=====ServerSocket  is startting.... prot is " + serverPort
    try while (true) socketQueue.offer(serverSocket.accept()) catch {
      case e: Exception => e.printStackTrace()
    } finally if (serverSocket != null && !serverSocket.isClosed()) serverSocket.close()

    //      while (true) {
    //        val client = serverSocket.accept()
    //        val remoteInfo = client.getRemoteSocketAddress
    //        val machineInfo = remoteInfo.asInstanceOf[InetSocketAddress]
    //        val machineAddress = machineInfo.getAddress
    //        // 封装socket 信息 打印相关信息
    //        val workerNode = new SocketClientBean()
    //        workerNode.setSocket(client)
    //        workerNode.setHostName(machineAddress.getHostName)
    //        workerNode.setHostIp(machineAddress.getHostAddress)
    //        workerNode.setPort(machineInfo.getPort)
    //        workerNode.setWorkInfo("socket client实例信息［" + workerNode.getHostName() + " " + workerNode.getHostIp() + ":" + workerNode.getPort() + "]")
    //        //        println(workerNode.getWorkInfo() + "已经注册为 client！")
    //        socketQueue.offer(client)
    //      }
  }

  /**
   * 加载属性配置文件
   */
  def loadProperties = {
    val fileseparator = System.getProperty("file.separator")
    val jarName = this.getClass.getProtectionDomain.getCodeSource.getLocation.getPath
    val jarpath = jarName.substring(0, jarName.lastIndexOf(fileseparator))
    // 默认配置文件：/../config/config.properties
    val propertiesFile = jarpath + "/../config/config.properties"
    val properties = new Properties
    properties.load(new FileInputStream(propertiesFile))
    properties
  }
}