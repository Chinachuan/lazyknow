package com.china.know.mq;

import com.china.know.config.MQConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * @ClassName: Producer01
 * @Description: TODO(发布订阅模式-创建一个生产者) 
 * @author: Jiuchuan.Shi
 * @Date: 2018年6月28日 下午3:46:11
 */
public class Producer {
	
	// 声明一个交换机的名称
	private static final String EXCHANGE_NAME = "test_exchange_fanout";
	
	public static void main(String[] args) throws Exception {
		// 拿到一个接连
		Connection connection = MQConnectionUtils.getConnection();
		// 创建通道
		Channel channel = connection.createChannel();
		// 声明交换机订阅类型
		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
		/**
		 * 每一个消费者 发送确认消息之前，消息队列不发送下一个消息到消费者，每次只处理一条消息
		 * 限制发送给同一个消费者，一次只发送一条   【公平分发模式】
		 */
		int prefetchCount = 1;
		channel.basicQos(prefetchCount);
		
		for (int i = 0; i < 10; i++) {
			// 创建消息
			String msg = "Hello,大家好，这是在分发的订阅消息 " + i;
			System.out.println("[消息] 生产者开始生产消息。。。");
			
			// 开始广播消息
			channel.basicPublish(EXCHANGE_NAME,"", null, msg.getBytes());
		}
		
		// 关闭通道
		channel.close();
		// 关闭接连
		connection.close();
	}

}
