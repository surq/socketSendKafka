1、使用示例：
http://localhost:8100/test_kafka?user=123&types=surongquqan
http://localhost:8100：为域名服务器。
test_kafka：kafka集群已有的topic。
“？” 后面为实时采集的要写入kafka的数据。

2、关于config/config.properties文件：
socket.server.port=必填项目
kafka.brokers.list=必填项目
kafka.producer.number=非必填项目，默认值10 producer个数
kafka.message.split.size=非必填项目，默认值：100条 （kafka批发送数据的限制）
kafka.message.split.time=非必填项目，默认值：10秒（kafka批发送数据的限制）
http.response.message=非必填项目