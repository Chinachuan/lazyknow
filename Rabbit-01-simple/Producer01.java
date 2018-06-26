package com.china.know.mq;

import com.china.know.config.MQConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * @ClassName: Producer01
 * @Description: TODO(创建一个生产者) 
 * @author: Jiuchuan.Shi
 * @Date: 2018年6月25日 下午1:34:31
 */
public class Producer01 {
	
	// 队列的名称
	private static final String QUEUENAME = "test_my_queue01";
	
	public static void main(String[] args) throws Exception {
		// 拿到一个接连
		Connection connection = MQConnectionUtils.getConnection();
		// 创建通道
		Channel channel = connection.createChannel();
		// 声明队列，简单理解就是给队列创建一个名字
		channel.queueDeclare(QUEUENAME, false, false, false, null);
		// 创建消息
		String msg = "Hello,大家好，这是我创建的第一条信息";
	
		// 开始广播消息
		channel.basicPublish("", QUEUENAME, null, msg.getBytes());
		// 关闭通道
		channel.close();
		// 关闭接连
		connection.close();
	}

}
