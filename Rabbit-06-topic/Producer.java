package com.china.topic;

import com.china.know.config.MQConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * @ClassName: Producer
 * @Description: TODO(消息队列之主题模式) 
 * @author: Jiuchuan.Shi
 * @Date: 2018年7月2日 下午1:35:19
 */
public class Producer {
	
	// 声明交换机的名称
	private static final String EXCHANGE_DIRECT = "exchange_topic";
	
	public static void main(String[] args) throws Exception {
		// 获得一个连接
		Connection connection = MQConnectionUtils.getConnection();
		// 创建一个通道
		Channel channel = connection.createChannel();
		// 声明交换机的订阅类型为 direct
		channel.exchangeDeclare(EXCHANGE_DIRECT, "topic");
		// 每次只接受一条消息
		int prefetchCount = 1;
		channel.basicQos(prefetchCount);
		
		String mesg  = "这是一条广播的消息";
		// 声明路由关键字
		String routingKey = "topic.add";
		// 广播消息
		channel.basicPublish(EXCHANGE_DIRECT, routingKey, null, mesg.getBytes());
		
		System.out.println("生产者已经广播了一条消息。");
		channel.close();
		connection.close();
	}

}
