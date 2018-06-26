package com.china.know.mq;

import com.china.know.config.MQConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;

/**
 * @ClassName: Consumer01
 * @Description: TODO(创建一个消费者) 
 * @author: Jiuchuan.Shi
 * @Date: 2018年6月25日 下午1:36:55
 */
public class Consumer01 {

	// 队列的名称
	private static final String QUEUENAME = "test_my_queue01";

	public static void main(String[] args) throws Exception {
		
		// 获得一个连接
		Connection connection = MQConnectionUtils.getConnection();
		// 创建通道
		Channel channel = connection.createChannel();
		// 声明队列
		channel.queueDeclare(QUEUENAME, false, false, false, null);
		
		/**
		 * 创建一个消费者
		 * 重写handleDeliver方法
		 * 为后续的监听做准备
		 */
		DefaultConsumer consumer = new DefaultConsumer(channel){
			public void handleDelivery(String consumerTag, 
					com.rabbitmq.client.Envelope envelope, 
					com.rabbitmq.client.AMQP.BasicProperties properties, 
					byte[] body) throws java.io.IOException {
				
				// 把字节转换成字符串
				String str = new String(body, "utf-8");
				
				System.out.println("輸入的信息是：" + str);
				
			};
		};
		
		// 队列监听
		channel.basicConsume(QUEUENAME, true, consumer);
		
	}

}
