package com.china.tx;

import com.china.know.config.MQConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * @ClassName: Producer
 * @Description: TODO(消息队列之事务) 
 * @author: Jiuchuan.Shi
 * @Date: 2018年7月2日 下午5:35:19
 */
public class Producer {
	
	// 声明队列的名称
	private static final String QUEUE_NAME = "test_exchange_tx";
	
	public static void main(String[] args) throws Exception {
		// 获得一个连接
		Connection connection = MQConnectionUtils.getConnection();
		// 创建一个通道
		Channel channel = connection.createChannel();
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		String mesg  = "这是一条广播的消息";
		try {
			// 声明事务
			channel.txSelect();
			// 广播消息
			channel.basicPublish("", QUEUE_NAME, null, mesg.getBytes());
			// 提交事务
			channel.txCommit();
			System.out.println("生产者已经广播了一条消息。");
			
		} catch (Exception e) {
			// 提交失败，回滚事务
			channel.txRollback();
			System.out.println("事务已回滚。。。" + e.getMessage());
		}
		
		channel.close();
		connection.close();
	}

}
