# Java version of flowing-retail/rest

This example demonstrates stateful resilience patterns in a REST environment. A payment (micro-)service can retrieve payments if called via REST. It requires an upstream REST service to charge credit cards.

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

Now you can run the payment service itself

```
mvn exec:java
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