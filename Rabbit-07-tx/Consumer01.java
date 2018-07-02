package com.china.tx;

import java.io.IOException;

import com.china.know.config.MQConnectionUtils;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * @ClassName: Consumer01
 * @Description: TODO(消息队列之事务) 
 * @author: Jiuchuan.Shi
 * @Date: 2018年7月2日 下午5:40:11
 */
public class Consumer01 {
	
	// 声明队列的名称
	private static final String QUEUE_NAME = "test_exchange_tx";

	public static void main(String[] args) throws Exception {
		Connection connection = MQConnectionUtils.getConnection();
		final Channel channel = connection.createChannel();
		// 首先给队列名一个名称
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);

		channel.basicConsume(QUEUE_NAME, false,new DefaultConsumer(channel){
			
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, 
					byte[] body) throws IOException {
				
				// 打印接受到的信息
				System.out.println("消费者01接受到的消息为:" + new String(body,"utf-8"));
				
			};
			
		} );
		
	}

}
