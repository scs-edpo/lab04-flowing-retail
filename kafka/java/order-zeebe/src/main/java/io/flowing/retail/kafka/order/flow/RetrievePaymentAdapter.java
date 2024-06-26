package io.flowing.retail.kafka.order.flow;

import java.util.Collections;
import java.util.UUID;

import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.flowing.retail.kafka.order.domain.Order;
import io.flowing.retail.kafka.order.flow.payload.RetrievePaymentCommandPayload;
import io.flowing.retail.kafka.order.messages.Message;
import io.flowing.retail.kafka.order.messages.MessageSender;
import io.flowing.retail.kafka.order.persistence.OrderRepository;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;

@Component
public class RetrievePaymentAdapter {
  
  @Autowired
  private MessageSender messageSender;
  
  @Autowired
  private OrderRepository orderRepository;  

  @JobWorker(type = "retrieve-payment")
  public void handle(JobClient client, ActivatedJob job) {
    OrderFlowContext context = OrderFlowContext.fromMap(job.getVariablesAsMap());
    
    Order order = orderRepository.findById(context.getOrderId()).get();   
            
    // generate an UUID for this communication
    String correlationId = UUID.randomUUID().toString();

    messageSender.send( //
        new Message<RetrievePaymentCommandPayload>( //
            "RetrievePaymentCommand", //
            context.getTraceId(), //
            new RetrievePaymentCommandPayload() //
              .setRefId(order.getId()) //
              .setReason("order") //
              .setAmount(order.getTotalSum())) //
        .setCorrelationid(correlationId));
    
    client.newCompleteCommand(job.getKey()) //
        .variables(Collections.singletonMap("CorrelationId_RetrievePayment", correlationId)) //
        .send().join();
  }

}
