package io.flowing.retail.paymentzeebe.rest;

import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.Collections;
import java.util.UUID;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;
import io.camunda.zeebe.client.api.worker.JobWorker;
import io.camunda.zeebe.model.bpmn.Bpmn;
import io.camunda.zeebe.model.bpmn.BpmnModelInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Step3: Use Zeebe state machine for long-running retry
 */
@RestController
public class PaymentRestHacksControllerV3 {

  @Autowired
  private ZeebeClient zeebe;
  
  @Autowired
  private ChargeCreditCardHandler handler;

  private JobWorker worker;

  @PostConstruct
  public void createFlowDefinition() {
    BpmnModelInstance flow = Bpmn.createExecutableProcess("paymentV3") //
        .startEvent() //
        .serviceTask("stripe").zeebeJobType("charge-creditcard-v3") //
            .zeebeJobRetries("2") //
        .endEvent().done();
    
    zeebe.newDeployResourceCommand() //
      .addProcessModel(flow, "payment.bpmn") //
      .send().join();

    worker = zeebe.newWorker()
        .jobType("charge-creditcard-v3") // 
        .handler(handler) // 
        .open();  
  }
  
  @Component
  public static class ChargeCreditCardHandler implements JobHandler {

    @Autowired
    private RestTemplate rest;
    private String stripeChargeUrl = "http://localhost:8099/charge";

    @Override
	public void handle(JobClient client, ActivatedJob job) throws Exception {
      CreateChargeRequest request = new CreateChargeRequest();
      request.amount = (int) job.getVariablesAsMap().get("amount");

      CreateChargeResponse response = new HystrixCommand<CreateChargeResponse>(HystrixCommandGroupKey.Factory.asKey("stripe")) {
        protected CreateChargeResponse run() throws Exception {
            return rest.postForObject( //
              stripeChargeUrl, //
              request, //
              CreateChargeResponse.class);
        }
      }.execute();
      
      client.newCompleteCommand(job.getKey()) //
        .variables(Collections.singletonMap("paymentTransactionId", response.transactionId))
        .send().join();
    }

  }

  @RequestMapping(path = "/payment/v3", method = PUT)
  public String retrievePayment(String retrievePaymentPayload, HttpServletResponse response) throws Exception {
    String traceId = UUID.randomUUID().toString();
    String customerId = "0815"; // get somehow from retrievePaymentPayload
    long amount = 15; // get somehow from retrievePaymentPayload

    chargeCreditCard(customerId, amount);

    response.setStatus(HttpServletResponse.SC_ACCEPTED);
    return "{\"status\":\"pending\", \"traceId\": \"" + traceId + "\"}";
  }

  public void chargeCreditCard(String customerId, long remainingAmount) {
    zeebe.newCreateInstanceCommand() //
      .bpmnProcessId("paymentV3")
      .latestVersion()
      .variables(Collections.singletonMap("amount", remainingAmount))
      .send().join();
  }
  
  public static class CreateChargeRequest {
    public int amount;
  }

  public static class CreateChargeResponse {
    public String transactionId;
  }

  @PreDestroy
  public void closeSubscription() {
    worker.close();
  }

}