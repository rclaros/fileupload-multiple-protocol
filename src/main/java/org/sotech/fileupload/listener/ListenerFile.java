package org.sotech.fileupload.listener;

import org.sotech.fileupload.bean.Documento;
import org.sotech.fileupload.client.ClientManager;
import org.sotech.fileupload.config.ApplicationConfig;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import org.slf4j.LoggerFactory;

public class ListenerFile implements MessageListener {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ListenerFile.class);

    @Override
    public synchronized void onMessage(Message msg) {
        if (msg instanceof ObjectMessage) {
            ObjectMessage objM = (ObjectMessage) msg;
            try {
                Documento doc = (Documento) objM.getObject();
                if (ApplicationConfig.status == true && doc.getUserTo().toLowerCase().equals(ApplicationConfig.me.getId().toLowerCase())) {
                    System.out.println("for me");
                    ClientManager.download(doc);
                }
            } catch (JMSException e) {
                System.out.println("onMessage " + e.getMessage());
            }
        }
    }
}
