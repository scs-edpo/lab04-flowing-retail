package io.flowing.retail.zeebe.payment;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PaymentAdapter {

  @JobWorker(type="retrieve-payment-z")
  public void retrievePayment(JobClient client, ActivatedJob job) {
    try {

      Map<String, Object> variables = job.getVariablesAsMap();
      String traceId = (String) variables.get("traceId");

      System.out.println("retrieved payment for "+traceId);
    } catch (Exception e) {
      throw new RuntimeException("Could not parse payload: " + e.getMessage(), e);
    }

    client.newCompleteCommand(job.getKey()).send()
            .exceptionally( throwable -> { throw new RuntimeException("Could not complete job " + job, throwable); });
  }

}
