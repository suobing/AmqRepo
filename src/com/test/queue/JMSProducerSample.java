package com.test.queue;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;


public class JMSProducerSample {
	//默认连接用户名
    private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;
    //默认连接密码
    private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
    //默认连接地址
//    private static final String BROKEURL = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static final String BROKEURL = "tcp://localhost:61616";//"tcp://192.168.214.129:61616"; //可以多个url用,隔开
    //发送的消息数量
    private static final int SENDNUM = 3;
    
    public static void main(String[] args) {
    	//连接工厂
		ConnectionFactory connectionFactory= new ActiveMQConnectionFactory(USERNAME, PASSWORD, BROKEURL);
		//连接 Connection表示在客户端和JMS系统之间建立的链接（对TCP/IP socket的包装）
		Connection conn = null;
		//会话 接受或者发送消息的线程
		Session session;
		//目的地  ，消息队列
		Destination destination;
		//消息生产者
		MessageProducer messageProducer;
		
		try{
            //通过连接工厂获取连接
            conn = connectionFactory.createConnection();
            //启动连接
//            conn.start();//不启动也能发送...
            //创建session
            session = conn.createSession(true, Session.AUTO_ACKNOWLEDGE);
            //创建一个名称为HelloWorld的消息队列
            destination = session.createQueue("testQueue");
            //创建消息生产者
            messageProducer = session.createProducer(destination);
            //发送消息
            sendMessage(session, messageProducer);

            session.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(conn!=null){
				try {
					conn.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void sendMessage(Session session, MessageProducer messageProducer) throws Exception{
        for (int i = 0; i < SENDNUM; i++) {
            //创建一条文本消息 
            TextMessage message = session.createTextMessage("AMQ 发送消息" +i);
            System.out.println(message.getText());
            //通过消息生产者发出消息 
            messageProducer.send(message);
        }		
	}
}
