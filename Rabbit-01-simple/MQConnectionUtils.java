package com.china.know.config;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @ClassName: MQConnectionUtils
 * @Description: TODO(创建连接) 
 * @author: Jiuchuan.Shi
 * @Date: 2018年6月25日 上午11:58:26
 */
public class MQConnectionUtils {
	
	public static Connection getConnection() throws Exception{
		// 创建一个连接工厂
		ConnectionFactory factory = new ConnectionFactory();
		// 声明本地服务
		factory.setHost("localhost");
		// 设置端口号
		factory.setPort(5672);
		// 设置一个MQ连接库
		factory.setVirtualHost("/vhost_db");
		// 设置用户名
		factory.setUsername("user_chuan");
		// 设置密码
		factory.setPassword("123");
		// 返回一个连接
		return factory.newConnection();	
	}

}
