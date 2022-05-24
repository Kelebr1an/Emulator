package ru.tk.ms.fts.emul.customer.reg.configuration;

import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.msg.client.jms.JmsConstants;
import com.ibm.msg.client.wmq.common.CommonConstants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

@Configuration
public class MQConfiguration {

    @Value("${mq.queueManager}")
    private String queueManager;

    @Value("${mq.channel}")
    private String channel;

    @Value("${mq.connName}")
    private String connName;

    @Value("${mq.user}")
    private String user;

    @Value("${mq.password}")
    private String password;

    @Value("${mq.queues.out}")
    private String queueOut;

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() throws JMSException {
        var factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());

        return factory;
    }

    @Bean
    public ConnectionFactory connectionFactory() throws JMSException {
        var connectionFactory = new MQConnectionFactory();
        connectionFactory.setQueueManager(queueManager);
        connectionFactory.setChannel(channel);
        connectionFactory.setConnectionNameList(connName);
        connectionFactory.setStringProperty(JmsConstants.USERID, user);
        connectionFactory.setStringProperty(JmsConstants.PASSWORD, password);
        connectionFactory.setTransportType(CommonConstants.WMQ_CM_CLIENT);

        return connectionFactory;
    }

    @Bean
    @Qualifier("queueTemplate")
    public JmsTemplate jmsMQTemplate() throws JMSException {
        var jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactory());
        jmsTemplate.setDefaultDestinationName(queueOut);

        return jmsTemplate;
    }

}
