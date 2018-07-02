package com.china.topic;

import java.io.IOException;

import com.china.know.config.MQConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;

/**
 * @ClassName: Consumer01
 * @Description: TODO(主题模式创建第一个消费者) 
 * @author: Jiuchuan.Shi
 * @Date: 2018年7月2日 下午1:40:11
 */
public class Consumer01 {
	
	// 声明队列的名称
	private static final String QUQUE_NAME = "queue_topic_name01";
	// 交换机名称
	private static final String EXCHANGE_DIRECT = "exchange_topic";

	public static void main(String[] args) throws Exception {
		Connection connection = MQConnectionUtils.getConnection();
		final Channel channel = connection.createChannel();
		// 首先给队列名一个名称
		channel.queueDeclare(QUQUE_NAME, false, false, false, null);
		/**
		 * 绑定队列名称
		 * 绑定交换机名称
		 * 声明路由规则的名称
		 */
		channel.queueBind(QUQUE_NAME, EXCHANGE_DIRECT, "topic.#");
		
		// 只接受一条消息
		int prefetchCount = 1;
		channel.basicQos(prefetchCount);
		
		Consumer consumer = new DefaultConsumer(channel){
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, 
					BasicProperties properties, byte[] body)throws IOException {
				// TODO Auto-generated method stub
				String mesg = new String(body, "utf-8");
				System.out.println("消费者01 接受到消息: " + mesg);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}finally{
					channel.basicAck(envelope.getDeliveryTag(), false);
				}
			}
		};
		
		channel.basicConsume(QUQUE_NAME, false, consumer);
		
	}

}
