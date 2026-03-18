# Java version of flowing-retail/rest (Camunda 7)

This variant demonstrates **stateful resilience patterns** in a REST environment using **Camunda 7**. A payment (micro-)service can retrieve payments if called via REST, and it requires an upstream REST service to charge credit cards.

![REST callstack](../../../docs/resilience-patterns/situation.png)

**See [introduction](../../README.md) for the storyline and patterns behind the examples.**

# Concrete technologies/frameworks in this folder

* Java
* Spring Boot
* Hystrix
* Camunda 7.x

# How-to run

## 1. Start the Stripe Fake Server

Run from `rest/` (build first, then run):

```
mvn -f java/stripe-fake/ clean package
mvn -f java/stripe-fake/ exec:java
```

After startup, choose the mode in the same terminal:

* `N` = Normal mode
* `S` = Slow mode (adds random delay, up to 60 seconds)

## 2. Start the Camunda 7 Payment Service

Run from `rest/` (build first, then run):

```
mvn -f java/payment-camunda clean package
mvn -f java/payment-camunda exec:java
```

## 3. Start the External Task Worker (required for `v5`, `v6`, `v7`)

For models that use Camunda **External Tasks** (e.g. asynchronous work distribution and compensation flows), start the worker in a separate terminal.

* Worker: `src/main/java/io/flowing/retail/payment/resthacks/worker/CustomerCreditWorker.java`

Run from `rest/` (make sure payment-camunda is already built):

```
mvn -f java/payment-camunda exec:java@customer-credit-worker
```

## 4. Supported payment versions

* `/payment/v1`
* `/payment/v2`
* `/payment/v3`
* `/payment/v3b`
* `/payment/v4`
* `/payment/v4b`
* `/payment/v5`
* `/payment/v6`
* `/payment/v7`

## 5. Quick test

```
curl \
-H "Content-Type: application/json" \
-X PUT \
-d '{}' \
http://localhost:8100/payment/v1
```

> This service uses port `8100`.