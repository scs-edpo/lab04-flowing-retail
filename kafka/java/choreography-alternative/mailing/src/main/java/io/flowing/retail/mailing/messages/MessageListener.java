package io.flowing.retail.mailing.messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {

    @Autowired
    private MessageSender messageSender;

    @KafkaListener(id = "mailing", topics = "flowing-retail")
    public void orderCompletedEventReceived(String messageJson, @Header("type") String messageType) throws Exception {
        if ("OrderCompletedEvent".equals(messageType)) {
            System.out.println("Received: " + messageJson);
        }
    }

    //TODO: Add more methods for other message types
    //TOdO: enhance readMe to explain the process and how it needs to be run
}
