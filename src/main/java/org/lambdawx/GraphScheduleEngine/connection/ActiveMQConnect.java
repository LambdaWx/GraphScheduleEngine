package org.lambdawx.GraphScheduleEngine.connection;

import com.google.common.io.Resources;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

import javax.jms.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * Created by L on 2016/11/21.
 */
public class ActiveMQConnect {
    private static Logger log = Logger.getLogger(ActiveMQConnect.class);
    private static  ConnectionFactory connectionFactory;
    static {
        init();
    }
    public static void init(){
        Properties prop = new Properties();
        InputStream is = null;
        try {
            URL url = Resources.getResource("hiveJdbc.properties");
            is = url.openStream();
            prop.load(is);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            log.warn("no hiveJdbc.properties found, using default settings");
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        String url = prop.getProperty("ActiveMQConnection.UR",ActiveMQConnection.DEFAULT_BROKER_URL);
        String user = prop.getProperty("ActiveMQConnection.USER",ActiveMQConnection.DEFAULT_USER);
        String password = prop.getProperty("ActiveMQConnection.PASSWORD",ActiveMQConnection.DEFAULT_PASSWORD);

        connectionFactory = new ActiveMQConnectionFactory(
                user,
                password,
                url);
    }
    public static Connection getConnection() throws Exception{
        Connection connection = connectionFactory.createConnection();
        // 启动
        connection.start();
        return connection;
    }

    public static Session getSession(Connection connection) throws Exception{
        Session session = connection.createSession(Boolean.FALSE,
                Session.AUTO_ACKNOWLEDGE);
        return session;
    }

    public static MessageConsumer getMessageConsumer(String destination, Session session) throws Exception{
        MessageConsumer consumer = null;
        try {
            Destination destinationObj = session.createQueue(destination);

            consumer = session.createConsumer(destinationObj);

        }catch (Exception e){
            e.printStackTrace();
        }
        return consumer;
    }
    public static MessageProducer getMessageProducer(String destination, Session session) throws Exception{
        Destination destinationObj = session.createQueue(destination);
        MessageProducer producer = session.createProducer(destinationObj);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        return producer;
    }
}
