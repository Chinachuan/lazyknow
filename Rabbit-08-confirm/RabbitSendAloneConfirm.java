package com.china.confirm;

import com.china.know.config.MQConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * @ClassName: RabbitSendAloneConfirm
 * @Description: TODO(消息队列单条确认模式) 
 * @author: Jiuchuan.Shi
 * @Date: 2018年7月5日 下午7:20:16
 */
public class RabbitSendAloneConfirm {
	
	// 声明队列的名称
	private static final String QUEUE_NAME = "test_queue_confirm01";
	
	public static void main(String[] args) throws Exception {
		// 获得一个连接
		Connection connection = MQConnectionUtils.getConnection();
		// 创建一个通道
		Channel channel = connection.createChannel();
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		
		channel.confirmSelect();
		
		String mesg  = "这是一条广播的消息";
		
		channel.basicPublish("", QUEUE_NAME, null, mesg.getBytes());
		
		// 等待消息回执，是否发送成功
		if(channel.waitForConfirms()){
			System.out.println("## 生产者--已成功发送消息！！！");
		}else{
			System.out.println("## 生产者--发送消息失败！！！");
		}
		
		channel.close();
		connection.close();
	}

}
