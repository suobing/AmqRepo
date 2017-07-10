package com.test.topic;


import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

public class TopicPublisher {
    public static void main(String[] args) throws JMSException {  
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");  
        Connection connection = factory.createConnection();  
        connection.start();  
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);  
        Topic topic = session.createTopic("myTopic.messages");  
        MessageProducer producer = session.createProducer(topic);  
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);  
        for(int i =0;i<5;i++){  
            TextMessage message = session.createTextMessage();  
            message.setText("message_" + i + System.currentTimeMillis());  
            producer.send(message);  
            System.out.println("Sent message: " + i+ message.getText());  
            try {  
                Thread.sleep(1000);  
            } catch (InterruptedException e) {  
                e.printStackTrace();  
            }  
        }  
    session.close();  
    connection.stop();  
    connection.close();  
		
	}
}
