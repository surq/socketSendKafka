package com.moxiu.util
import kafka.producer.KeyedMessage
import scala.collection.mutable.HashMap
import kafka.producer.Producer
import kafka.producer.ProducerConfig
import java.util.Properties
import scala.collection.mutable.ArrayBuffer

class KafkaProducer {
  val brokers = SocketServer.properties.getProperty("kafka.brokers.list")
  val producermap = new HashMap[String, Producer[String, String]]()
  /**
   * kafa 发送message
   */
  def kafkaSend(topic: String, msg: String): Unit = try {
    getProducer.send(new KeyedMessage[String, String](topic, msg.hashCode.toString, msg))
  } catch {
    case e: Exception => e.printStackTrace()
  }

  /**
   * kafa 发送message
   * msgList: List[(topic, message)]
   */
  def kafkaSendList(msgList: List[(String, String)]): Unit = {
    val messageList = ArrayBuffer[KeyedMessage[String, String]]()
    // topic ,key,message
    msgList.map(msg => messageList += new KeyedMessage[String, String](msg._1, msg._2.hashCode.toString, msg._2))
    try {
      getProducer.send(messageList: _*)
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }

  def getProducer: Producer[String, String] = {
    producermap.getOrElse(brokers, {
      val props = getProducerConfig
      val pro = new Producer[String, String](new ProducerConfig(props))
      producermap.put(brokers, pro)
      pro
    })
  }

  def getProducerConfig: Properties = {
    val props = new Properties()
    props.put("metadata.broker.list", brokers)
    props.put("serializer.class", "kafka.serializer.StringEncoder")
    props
  }
}