package io.flowing.retail.email.application;

import org.springframework.stereotype.Component;


@Component
public class EmailService {

    public void createEmail(String pickId, String recipientName, String recipientAddress, String logisticsProvider) {
        //TODO: replace simulated email with actual email sent
        System.out.println("Email to " + recipientName + "\n\n" + recipientAddress + "\n\n" + pickId + "\n\n" + logisticsProvider);
    }

}
