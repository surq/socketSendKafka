package com.moxiu.util

/**
 * @author 宿荣全
 * @date: 2018-11-19
 *  发送kafka message List
 */
class MessageSentTasker(msgList: List[(String, String)]) extends Runnable {
  override def run = (new KafkaProducer).kafkaSendList(msgList)
}