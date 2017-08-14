package com.test.queue;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class JMSConsumerSample {
	
    private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;//默认连接用户名
    private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;//默认连接密码
//    private static final String BROKEURL = ActiveMQConnection.DEFAULT_BROKER_URL;//默认连接地址	
    private static final String BROKEURL = "tcp://localhost:61616";//"tcp://192.168.214.129:61616";
	public static void main(String[] args) {
		//实例化连接工厂
		ConnectionFactory connectionFactory= new ActiveMQConnectionFactory(USERNAME, PASSWORD, BROKEURL);
		Connection conn = null;//连接
		Session session = null;//会话 接受或者发送消息的线程
		Destination destination;//消息目的地
		MessageConsumer messageConsumer;//消息消费者
		
		try {
			//通过工厂获取连接，ActiveMQConnection
			conn = connectionFactory.createConnection();
			//启动连接
			conn.start();
			//创建session ，ActiveMQSession
			session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			//创建队列
			destination = session.createQueue("testQueue");
			//创建消费者，ActiveMQMessageConsumer, AMQ界面上才会显示挂上Consumer
			messageConsumer = session.createConsumer(destination);
			
//			syncReceiveMessage(messageConsumer);
			asyncOnMessage(messageConsumer);
			
		} catch (JMSException e) {
			e.printStackTrace();
		}finally{//接收端不要关session和connection
        	if(session!=null){
        		try {
					session.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
        	}
			if(conn!=null){
				try {
					conn.stop();
					conn.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//同步接收消息
	public static void syncReceiveMessage(MessageConsumer messageConsumer) throws JMSException{
		while(true){
			TextMessage textMessage = (TextMessage) messageConsumer.receive(10000);//同步,阻塞; 消息超时时间
			if(textMessage != null){
				System.out.println("receive到的消息： "+textMessage.getText());
			}else{
				break;
			}
		}	
	}
	
	//异步接收消息
	public static void asyncOnMessage(MessageConsumer messageConsumer) throws JMSException{
		messageConsumer.setMessageListener(new MessageListener(){
			@Override
			public void onMessage(Message arg0) {
				if(arg0 instanceof TextMessage){
					System.out.println("onMessage到的消息： "+(TextMessage)arg0);
				}
			}
			}
		);
	}
}
