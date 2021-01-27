# Service for testing Apache Camel with RabbitMQ 
Library name: camel-rabbit

  Для примера работы с очередью по протоколу AMQP была использована библиотека apache camel и брокер сообщений RabbitMQ.  
В примере создается одна входящая очередь (inputqueue) и одна исходящая (outputorder).
Для тестов используется JMeter. Необходимя конфигурация для JMeter выложена в соответсвующей папке: RabbitMQ_Camel.jmx
По умолчанию, в JMeter создается 5 сообщений из 10 потоков.
В параметрах входящей очереди максимальное количество потоков threadPoolSize = 20.

Для проведения тестирования необходимо сначала запустить RabbitMQ.
Затем запустить сервис camel-rabbit. После этого открыть приложенную конфигурацию в JMeter и запуcтить выполнение.

После обработки сообщений в логе выводиться информация:
 route1 - ******** ROUTING FROM INPUT QUEUE TO OUTPUT
 route2 - ******** PROCESS MESSAGE FROM OUTPUT QUEUE
 CamelConsumer - START CONSUME MESSAGE, docId: 1 docType: order
 CamelConsumer - FINISH CONSUME MESSAGE, docId: 1 docType: order

Ссылки на использованную документацию:
https://camel.apache.org/manual/latest/pojo-consuming.html

https://guilhermebmilagre.medium.com/spring-boot-2-x-apache-camel-rabbitmq-5339f7848a87

JMeter:
Установка плагина Rabbit 
https://martkos-it.co.uk/blog/jmeter-amp-rabbit-mq

Запуск
https://jmeter.apache.org/usermanual/get-started.html

Трудности при реализации:
У каждой очереди должен быть свой exchangeName. Если будет одинаковый, то пересылка сообщений будет зацикливаться.

## Example
java -jar camel-rabbit-1.0.0.jar

## Build
mvn clean install
