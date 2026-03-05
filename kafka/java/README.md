# Flowing Retail / Apache Kafka / Java

This folder contains services written in Java that connect to Apache Kafka as means of communication between the services.

Tech stack:

* Java
* Spring Boot
* Apache Kafka

For the **workflow engine** you can decide between:
* Camunda (folder \*-camunda)
* Zeebe (folder \*-zeebe)

![Microservices](../../docs/kafka-services.png)

# Run the application

You can either
* 
* Build (Maven) and start via Docker Compose
* Build (Maven) and start manually (including Kafka)

The default is that Camunda is used in these settings. When you want to use Zeebe you have to manually build and run it.

## Docker Compose with Docker images

* Download [docker-compose-kafka-java-order-camunda.yml](../../runner/docker-compose/docker-compose-kafka-java-order-camunda.yml) or clone this repo and goto [docker-compose/](../../runner/docker-compose/)
* Goto directory where you downloaded this file

```
cd docker-compose
```

* Start using docker compose:

```
docker compose -f docker-compose-kafka-java-order-camunda.yml up
```

* After everything has started up you are ready to visit the overview page [http://localhost:8099](http://localhost:8089)
* You can place an order via [http://localhost:8091](http://localhost:8091)
* You can inspect insides of Order via [http://localhost:8092](http://localhost:8092)
* You can inspect insides of Payment via [http://localhost:8093](http://localhost:8093)
* You can inspect all events going on via [http://localhost:8095](http://localhost:8095)

If you like you can connect to Kafka from your local Docker host machine too. 

Note that there are a couple of other docker-compose files available too, e.g. to play around with the choreography.


## Docker Compose with local build of Docker images

* Download or clone the source code
* Run a full maven build

```
mvn install
```

* Build Docker images and start them up

```
docker compose build
docker compose up
```

* After everything has started up you are ready to visit the overview page [http://localhost:8099](http://localhost:8089)
* You can place an order via [http://localhost:8091](http://localhost:8091)
* You can inspect insides of Order via [http://localhost:8092](http://localhost:8092)
* You can inspect insides of Payment via [http://localhost:8093](http://localhost:8093)
* You can inspect all events going on via [http://localhost:8095](http://localhost:8095)

If you like you can connect to Kafka from your local Docker host machine too. 


