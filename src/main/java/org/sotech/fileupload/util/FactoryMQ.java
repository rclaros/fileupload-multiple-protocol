package org.sotech.fileupload.util;

import org.sotech.fileupload.bean.Documento;
import org.sotech.fileupload.client.ClientManager;
import org.sotech.fileupload.config.ApplicationConfig;
import org.sotech.fileupload.listener.ListenerFile;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;

public class FactoryMQ {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(FactoryMQ.class);
    private static ConnectionFactory connectionFactory;
    private static Connection connection = null;
    private static Session session = null;

    static {
        try {
            System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES", "*");
            connectionFactory = new ActiveMQConnectionFactory(ApplicationConfig.param_queue_url);
            System.out.println("queue conected");
        } catch (Exception e) {
            log.error("static start class FactoryMQ ({},{})", e.getMessage(), e);
        }
    }

    public static void start() {
        try {
            close();
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue(ApplicationConfig.param_queue_nombre);
            MessageConsumer consumer = session.createConsumer(queue);
            consumer.setMessageListener(new ListenerFile());
            connection.start();
        } catch (JMSException e) {
            System.out.println("start queue server " + e.getMessage());
        }
    }

    public static void sendMessage(Documento doc) {
        try {
            ObjectMessage msg = session.createObjectMessage();
            msg.setObject(doc);
            Queue queue = session.createQueue(ApplicationConfig.param_queue_nombre);
            MessageProducer producer = session.createProducer(queue);
            producer.send(msg);
        } catch (JMSException e) {
            System.out.println("sendMessage " + e.getMessage());
        }
    }

    public static void sendMessageServer(Documento doc) throws JMSException {
        ObjectMessage msg = session.createObjectMessage();
        msg.setObject(doc);
        Queue queue = session.createQueue(ApplicationConfig.param_queue_admin);
        MessageProducer producer = session.createProducer(queue);
        producer.send(msg);
    }

    public static void postData(Documento doc) {
        try {
            JSONObject json = new JSONObject();
            json.put("id", doc.getId());
            json.put("userfrom", doc.getUserFrom());
            json.put("userto", doc.getUserTo());
            json.put("filename", doc.getFilename());
            json.put("protocol", doc.getProtocol());
            json.put("checksum", doc.getChecksum());
            if (ClientManager.upload.isOpen()) {
                ClientManager.upload.send(json.toString());
            }
        } catch (RuntimeException e) {
            System.out.println("postData " + e.getMessage());
        }
    }

    public static void close() {
        try {
            if (session != null) {
                session.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (JMSException e) {
        }
    }
}
