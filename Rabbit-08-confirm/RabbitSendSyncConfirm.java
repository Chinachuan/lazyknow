package com.china.confirm;


import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import com.china.know.config.MQConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;

/**
 * @ClassName: RabbitSendAloneConfirm
 * @Description: TODO(消息队列批量确认模式) 
 * @author: Jiuchuan.Shi
 * @Date: 2018年7月5日 下午7:20:16
 */
public class RabbitSendSyncConfirm {
	
	// 声明队列的名称
	private static final String QUEUE_NAME = "test_queue_confirm03";
	
	public static void main(String[] args) throws Exception {
		// 获得一个连接
		Connection connection = MQConnectionUtils.getConnection();
		// 创建一个通道
		Channel channel = connection.createChannel();
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		// 选择通道模式为消息确认机制
		channel.confirmSelect();
		
		final SortedSet<Long> confirmSet = Collections.synchronizedSortedSet(new TreeSet<Long>());
		
		channel.addConfirmListener(new ConfirmListener() {
			
			// 有问题的消息
			@Override
			public void handleNack(long deliveryTag, boolean multiple) throws IOException {
				// 多条清空
				if(multiple){
					System.out.println("handleNack ---- 多条清空 ----multiple");
					confirmSet.headSet(deliveryTag + 1).clear();
				}else{
					System.out.println("handleNack ---- 单条 ----multiple");
					confirmSet.remove(deliveryTag);
				}
				
			}
			
			// 正确发送的消息
			@Override
			public void handleAck(long deliveryTag, boolean multiple) throws IOException {
				// TODO Auto-generated method stub
				// 多条清空
				if(multiple){
					System.out.println("handleAck ---- 多条清空 ----multiple");
					confirmSet.headSet(deliveryTag + 1).clear();
				}else{
					System.out.println("handleAck ---- 单条 ----multiple");
					confirmSet.remove(deliveryTag);
				}
				
			}
		});
		
		String mesg = "生产者开始异步生产消息了。。。";
		
		for (int i = 0; i < 20; i++) {
			long count = channel.getNextPublishSeqNo();
			channel.basicPublish("", QUEUE_NAME, null, mesg.getBytes());
			confirmSet.add(count);
		}
	}

}
