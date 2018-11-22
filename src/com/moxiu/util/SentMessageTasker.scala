package com.moxiu.util

/**
 * @author 宿荣全
 * @date: 2018-11-19
 *  发送kafka message List
 */
class SentMessageTasker extends Runnable {
  override def run = {
    val producer = new KafkaProducer
    while (true) producer.kafkaSendList(SocketServer.mesgPackageQueue.take)
  }
}