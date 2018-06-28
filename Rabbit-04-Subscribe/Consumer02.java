package com.china.know.mq;

import com.china.know.config.MQConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;

/**
 * @ClassName: Consumer02
 * @Description: TODO(发布订阅模式-创建一个消费者) 
 * @author: Jiuchuan.Shi
 * @Date: 2018年6月28日 下午5:06:59
 */
public class Consumer02 {

	// 队列的名称
	private static final String QUEUENAME = "my_queue_name02";
	// 交换机的名称
	private static final String EXCHANGE_NAME = "test_exchange_fanout";

	public static void main(String[] args) throws Exception {
		
		// 获得一个连接
		Connection connection = MQConnectionUtils.getConnection();
		// 创建通道
		final Channel channel = connection.createChannel();
		// 声明队列
		channel.queueDeclare(QUEUENAME, false, false, false, null);
		
		// 绑定队列到交换机转发器
		channel.queueBind(QUEUENAME, EXCHANGE_NAME, "");
		
		// 一次只发一个
		channel.basicQos(1);
		
		/**
		 * 创建一个消费者
		 * 重写handleDeliver方法
		 * 为后续的监听做准备
		 */
		DefaultConsumer consumer = new DefaultConsumer(channel){
			// 消息到达触发这个事件
			public void handleDelivery(String consumerTag, 
					com.rabbitmq.client.Envelope envelope, 
					com.rabbitmq.client.AMQP.BasicProperties properties, 
					byte[] body) throws java.io.IOException {
				
				// 把字节转换成字符串
				String str = new String(body, "utf-8");
				System.out.println("输出的信息是：" + str);
				
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					System.out.println("消费者02");
					// 公平分发模式
					channel.basicAck(envelope.getDeliveryTag(), false);
				}
				
			};
		};
		
		// 如果为true就是自动分发模式,为false是手动分发
		boolean autoAck = false;
		// 队列监听
		channel.basicConsume(QUEUENAME, autoAck, consumer);
		
	}

}
