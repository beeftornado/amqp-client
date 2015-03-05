package com.github.sstone.amqp.samples

import akka.actor.ActorSystem
import akka.dispatch.Future
import akka.util.duration._
import com.github.sstone.amqp.Amqp._
import com.github.sstone.amqp.RpcServer.{IProcessor, ProcessResult}
import com.github.sstone.amqp.{ConnectionOwner, RpcServer}
import com.rabbitmq.client.ConnectionFactory

import scala.concurrent.ExecutionContext
/**
 * start with mvn exec:java -Dexec.mainClass=com.github.sstone.com.github.sstone.amqp.amqp.samples.BasicRpcServer -Dexec.classpathScope="compile"
 */
object BasicRpcServer extends App {
  import scala.concurrent.ExecutionContext.Implicits.global

  implicit val system = ActorSystem("mySystem")

  // create an AMQP connection
  val connFactory = new ConnectionFactory()
  connFactory.setUri("com.github.sstone.amqp.amqp://guest:guest@localhost/%2F")
  val conn = system.actorOf(ConnectionOwner.props(connFactory, 1 second))

  val queueParams = QueueParameters("my_queue", passive = false, durable = false, exclusive = false, autodelete = true)

  // create a "processor"
  // in real life you would use a serialization framework (json, protobuf, ....), define command messages, etc...
  // check the Akka AMQP proxies project for examples
  val processor = new IProcessor {
    def process(delivery: Delivery) = Future {
      // assume that the message body is a string
      val input = new String(delivery.body)
      println("processing " + input)
      val output = "response to " + input
      ProcessResult(Some(output.getBytes("UTF-8")))
    }

    // likewise,  the same serialization framework would be used to return errors
    def onFailure(delivery: Delivery, e: Throwable) = ProcessResult(Some(("server error: " + e.getMessage).getBytes("UTF-8")))
  }

  ConnectionOwner.createChildActor(conn, RpcServer.props(queueParams, StandardExchanges.amqDirect,  "my_key", processor, ChannelParameters(qos = 1)))
}
