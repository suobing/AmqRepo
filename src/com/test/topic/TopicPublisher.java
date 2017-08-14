package com.test.topic;
/**
 * Topic模式下,provider保持运行,subscriber才会收到消息
 */

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
        //设置为persistent,重启AMQ,消息不丢失;默认为...
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        for(int i =0;i<7;i++){  
            TextMessage message = session.createTextMessage();  
            message.setText("message_" + i +" "+ System.currentTimeMillis());
            message.setStringProperty("property", "消息property");
            //发布topic消息
            producer.send(message);  
            System.out.println("Sent message: " + message.getText());  
//            try {  
//                Thread.sleep(1000);  
//            } catch (InterruptedException e) {  
//                e.printStackTrace();  
//            }  
        }  
//    session.commit();
    session.close();  
    connection.stop();  
    connection.close();  
		
	}
}
