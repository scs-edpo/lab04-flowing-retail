package io.flowing.retail.paymentzeebe.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfiguration {

  @Bean
  public RestTemplate createRestTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    return restTemplate;
  }
}
