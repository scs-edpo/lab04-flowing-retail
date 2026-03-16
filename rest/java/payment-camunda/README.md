# Java version of flowing-retail/rest

This example demonstrates **stateful resilience patterns** in a REST environment. A payment (micro-)service can retrieve payments if called via REST. It requires an upstream REST service to charge credit cards.

![REST callstack](../../../docs/resilience-patterns/situation.png)

This simple call-chain is great to demonstrate various important resilience patterns.

**See [introduction](../../) for the storyline / patterns behind**

# Concrete technologies/frameworks in this folder:

* Java
* Spring Boot
* Hystrix
* Camunda 7.x

# How-to run

First you have to startup the stripe fake server, as this handles the credit card payments.

```
mvn -f ../stripe-fake/ exec:java
```

After startup, the stripe fake service expects a mode input:

* `N` = Normal mode
* `S` = Slow mode (adds random delay, up to 60 seconds)

Type `S` or `N` in the same console where you started stripe fake and press Enter. You can switch between modes while it is running.


Now you can run the payment service itself

```
mvn exec:java
```

If you run process models that rely on Camunda **External Tasks** (see [Asynchronous work distribution without messaging](../../README.md#asynchronous-work-distribution-without-messaging) and [Business transactions using compensation](../../README.md#business-transactions-using-compensation), i.e. v5/v6/v7), run the worker in a separate terminal as well:

* Worker path: `src/main/java/io/flowing/retail/payment/resthacks/worker/CustomerCreditWorker.java`

Run this command from `rest/java/payment-camunda`:

```
mvn exec:java@customer-credit-worker
```



Now the different versions of the payment service are available:

* http://localhost:8100/payment/v1
* ...
* http://localhost:8100/payment/v6

You now can issue a PUT with an empty body:

```
curl \
-H "Content-Type: application/json" \
-X PUT \
-d '{}' \
http://localhost:8100/payment/v1
```