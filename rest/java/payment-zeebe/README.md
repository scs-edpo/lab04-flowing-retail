# Java version of flowing-retail/rest (Camunda 8 / Zeebe)

This variant demonstrates **stateful resilience patterns** in a REST environment using **Camunda 8 / Zeebe**.

![REST callstack](../../../docs/resilience-patterns/situation.png)

**See [introduction](../../README.md) for the storyline and patterns behind the examples.**

# Concrete technologies/frameworks in this folder

* Java
* Spring Boot
* Hystrix/Resilience4j 
* Camunda 8 / Zeebe

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

## 2. Choose and prepare your Zeebe backend

### Option A — Local Zeebe (Docker)

Run from `rest/`:

```
docker compose -f ../runner/docker-compose/docker-compose-kafka-zeebe-infra.yml up -d
```

`src/main/resources/application.properties` already contains the local defaults:

```properties
zeebe.client.broker.gateway-address=127.0.0.1:26500
zeebe.client.security.plaintext=true
```

### Option B — Camunda Cloud

Use the Camunda Cloud credentials provided on Canvas and update `src/main/resources/application.properties` to use your cluster:

```properties
zeebe.client.cloud.region=********
zeebe.client.cloud.clusterId=********
zeebe.client.cloud.clientId=********
zeebe.client.cloud.clientSecret=********

```

## 3. Start the Camunda 8 / Zeebe Payment Service

Run from `rest/` (build first, then run):

```
mvn -f java/payment-zeebe clean package
mvn -f java/payment-zeebe exec:java
```

## 4. Supported payment versions

* `/payment/v1`
* `/payment/v2`
* `/payment/v3`
* `/payment/v4`

## 5. Quick test

```
curl \
-H "Content-Type: application/json" \
-X PUT \
-d '{}' \
http://localhost:8100/payment/v1
```

> This service uses port `8100`, so make sure no other payment variant is already running on that port.