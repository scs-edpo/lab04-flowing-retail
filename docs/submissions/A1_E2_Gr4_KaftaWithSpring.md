# University of St.Gallen - Exercise Submission

## Course Information

- **Course:** Event-driven and Process-oriented Architectures FS2024
- **Instructors:** B. Weber, R. Seiger, A. Abbad-Andaloussi]()

## Deadline

- **Submission Date:** 05.03.2024; 23:59 CET

# Exercise 02: Kafka with Spring


### Implementation of an Event Notification Service in an Event-Driven Architecture

#### Decision
We have decided to introduce a notification service within our event-driven architecture for the 'flowing-retail' application. 
This service will be responsible for sending email notifications to customers, informing them about key milestones in their order processing, 
such as when their order is completed.

#### Rationale
The decision to implement this feature as a listener for the 'OrderCompletedEvent' on the 'flowing-retail' topic is twofold:

- It aligns with the event-driven nature of our system, ensuring that notifications are triggered by actual business events.
- It decouples the notification concern from the core order processing logic, which adheres to the principle of single responsibility and facilitates independent scaling and maintenance.

#### Design
Initially, the new service will subscribe to the "flowing-retail" Kafka topic and listen for 'OrderCompletedEvent' messages. 
Upon detecting an event, it will extract the necessary customer information (which is limited to the name at this point) 
from the event payload and send an email notification using a predefined template that includes relevant order details.
The use of a topic allows for a decoupling of the event producer and consumer, 
which means that any part of the system interested in this event can listen to this topic without direct interaction with the order management component.


#### Additional Considerations
While the current implementation focuses on 'OrderCompletedEvent', the service is designed to be extensible to 
accommodate additional event types for notification in the future, further leveraging the benefits of our event-driven architecture.








