package com.test.topic;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

public class TopicSubscriber {
    public static void main(String[] args) {  
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        Connection connection = null;
        Session session = null;
        try {  
            connection = factory.createConnection();  
            connection.start();  
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);  
            Topic topic = session.createTopic("myTopic.messages");  
            MessageConsumer consumer = session.createConsumer(topic);  
            consumer.setMessageListener(new MessageListener() {  
                public void onMessage(Message message) {  
                    TextMessage tm = (TextMessage) message;  
                    try {  
                        System.out.println("Received message: " + tm.getText());  
                    } catch (JMSException e) {  
                        e.printStackTrace();  
                    }  
                }  
            });  
        } catch (JMSException e) {  
            e.printStackTrace();  
        }finally{
        	if(session!=null){
        		try {
					session.close();
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        	if(connection!=null){
        		try {
        			connection.stop();
        			connection.close();
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        }  
    }  
}
