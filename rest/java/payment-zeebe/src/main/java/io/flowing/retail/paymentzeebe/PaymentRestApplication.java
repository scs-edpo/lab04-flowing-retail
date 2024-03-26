package io.flowing.retail.paymentzeebe;

import io.camunda.zeebe.client.ZeebeClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class PaymentRestApplication {

  @Bean
  public ZeebeClient zeebeClient() {
    ZeebeClient zeebeClient = ZeebeClient.newClientBuilder() //
    		.usePlaintext() // don't use SSL in default local broker
    		.build();
    return zeebeClient;
  }

  public static void main(String[] args) throws Exception {
    SpringApplication.run(PaymentRestApplication.class, args);
  }

}
