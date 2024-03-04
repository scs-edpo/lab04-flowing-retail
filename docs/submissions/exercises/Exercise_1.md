# E01 - Outage of Zookeeper

## E01-01: During Runtime

### Parameters and Process

- **Lab:** 02 Part2 Eye-tracking main.
- **Action:** Run the lab as provided and then shut down the Docker Container of Zookeeper.

### Observations

- **Outcome:** Both Producer and Consumer continue running as before.

### Conclusion

- Zookeeper is primarily responsible for setup. Therefore, shutting it down after the system is already running does not impact the currently running system.

## E01-02: Before Runtime

### Parameters and Process

- **Lab:** 02 Part2 Eye-tracking main.
- **Action:** Shut down the Docker Container of Zookeeper, then run the lab as provided.

### Observations

#### Producers
- creating topic: click-events
- `[kafka-admin-client-thread | adminclient-1] INFO org.apache.kafka.clients.NetworkClient - [AdminClient clientId=adminclient-1] Disconnecting from node 1001 due to request timeout.`
- `Cancelled createTopics request with correlation id 4 due to node 1001 being disconnected`
- Exited the program 
- Similar for both producers

#### Consumer

- Loaded the last state if records where present in Kafka from previous runs.
- `[main] INFO org.apache.kafka.clients.consumer.KafkaConsumer - [Consumer clientId=consumer-grp1-1, groupId=grp1] Subscribed to topic(s): gaze-events, click-events`
  - Subscription to topics seems to be successful since Kafka Container is up and subscription is possible.
- `Id=grp1] Error while fetching metadata with correlation id 34524 : {gaze-events=UNKNOWN_TOPIC_OR_PARTITION, click-events=UNKNOWN_TOPIC_OR_PARTITION}`
  - Continuously fails within the While-loop to poll for records
- Program keeps running

#### Kafka
- Kafka Container Logs:
  - `2024-03-02 14:01:47 [2024-03-02 13:01:47,928] WARN [Controller id=1001, targetBrokerId=1001] Connection to node 1001 (localhost/127.0.0.1:9092) could not be established. Broker may not be available. (org.apache.kafka.clients.NetworkClient)`
  - `2024-03-02 14:01:47 java.io.IOException: Connection to localhost:9092 (id: 1001 rack: null) failed.
    2024-03-02 14:01:47     at org.apache.kafka.clients.NetworkClientUtils.awaitReady(NetworkClientUtils.java:70)
    2024-03-02 14:01:47     at kafka.controller.RequestSendThread.brokerReady(ControllerChannelManager.scala:298)
    2024-03-02 14:01:47     at kafka.controller.RequestSendThread.doWork(ControllerChannelManager.scala:251)
    2024-03-02 14:01:47     at org.apache.kafka.server.util.ShutdownableThread.run(ShutdownableThread.java:130)
    2024-03-02 14:01:47 [2024-03-02 13:01:47,932] DEBUG [PartitionStateMachine controllerId=1001] Started partition state machine with initial state -> Map() (kafka.controller.ZkPartitionStateMachine)`


### Conclusion

- The observations indicate that shutting down Zookeeper before running the system affects the connectivity and functionality of the producers and consumer, highlighting Zookeeper's critical role in initial system setup and maintenance. Zookeeper serves in managing the Kafka cluster, including broker health checks, leader election, and maintaining overall cluster stability.
- The Kafka Container Logs indicate that the system is unable to establish a connection with the broker, which is a direct result of Zookeeper being down.
- Topics were created previously


# E02 - Impact of Load and Batch Size on Processing Latency 

## E02-01: ...

### Parameters and Process

- **Lab:** 02 Part2 Eye-tracking main.
- **Action:** Adjust the batch size in the clickStream-producer configuration: batch.size 
  - default is 16384 bytes (16KB)
  - set it to batch.size=65536 bytes (64KB) 

- Measure the latency by capturing the time when the record is sent and when the acknowledgment is received. Adding below code snippets to the ClicksProducer and EyeTrackerProducer.
```java
        List<Long> latencyList = new ArrayList<>();
        // ...
        long sendTimeNano = System.nanoTime(); // Capture send time
        // send the click event
        Future<RecordMetadata> future = producer.send(new ProducerRecord<String, Clicks>(
        topic, // topic
        clickEvent  // value
        ));

        RecordMetadata metadata = future.get(); // Blocks until the record is acknowledged
        long ackTimeNano = System.nanoTime(); // Capture ack time
        // Calculate and print the latency in milliseconds
        long latencyInMillis = (ackTimeNano - sendTimeNano) / 1_000_000;
        System.out.println("Latency: " + latencyInMillis + " ms");
        latencyList.add(latencyInMillis);
        //...
        if(counter == 100)
        break;
        // ...
        double averageLatency = latencyList.stream().mapToLong(Long::longValue).average().orElse(0);
        System.out.println("Average latency: " + averageLatency + " ms");
```

### Observations

#### EyeTrackersProducer

##### Baseline with default batch size of 16KB
- `...`
- `gazeEvent sent: eventID: 4997, timestamp: 1350418648235600, xPosition: 892, yPosition: 811, pupilSize: 3,  from deviceID: 0`
- `Latency: 3 ms`
- `gazeEvent sent: eventID: 4998, timestamp: 1350418658812100, xPosition: 748, yPosition: 884, pupilSize: 3,  from deviceID: 1`
- `Latency: 2 ms`
- `gazeEvent sent: eventID: 4999, timestamp: 1350418670381200, xPosition: 1482, yPosition: 1038, pupilSize: 3,  from deviceID: 0`
- **Average latency: 2.3608 ms**

##### With batch size of 64KB
- **Average latency: 2.282 ms**
- **Average latency: 2.3622 ms**

##### With batch size of 1KB
- **Average latency: 2.6022 ms**
- **Average latency: 2.3792 ms**

#### ClickStreamProducer

##### Baseline with default batch size of 16KB
- `...`
- `clickEvent sent: eventID: 97, timestamp: 1350611618473000, xPosition: 418, yPosition: 121, clickedElement: EL1,`
- `Latency: 3 ms`
- `clickEvent sent: eventID: 98, timestamp: 1350614728184900, xPosition: 1172, yPosition: 24, clickedElement: EL8,`
- `Latency: 6 ms`
- `clickEvent sent: eventID: 99, timestamp: 1350616888767700, xPosition: 681, yPosition: 686, clickedElement: EL11,`
- **Average latency: 5.49 ms**

##### With batch size of 64KB
- **Average latency: 5.47 ms**

##### With batch size of 1KB
- **Average latency: 6.32 ms**



### Conclusion
It seems that with smaller batch sizes, the latency increases. This is expected as the producer has to send more requests to the broker, which increases the time it takes to send the data. The default batch size of 16KB seems to be a good balance between latency and throughput.

# E03 - Setup of prometheus and grafana

- understand labels
  - https://kafka.apache.org/documentation/#monitoring
  - https://zookeeper.apache.org/doc/current/zookeeperJMX.html
  - https://www.datadoghq.com/blog/monitoring-kafka-performance-metrics/#metric-to-watch-underreplicatedpartitions
  - https://support.serverdensity.com/hc/en-us/articles/360001083446-Monitoring-Kafka
  - https://access.redhat.com/documentation/de-de/red_hat_amq/7.2/html/using_amq_streams_on_red_hat_enterprise_linux_rhel/monitoring-str

# E04 The risk of data loss due to offset misconfigurations

## E01-01: Read from 0

### Parameters and Process
- **Lab:** 02 Part2 Eye-tracking main.
- **Action:** Adjust the consumer to read from offset 0 and run the lab as provided.

### Observations
- The consumer reads all records from the beginning of the topic, including records that were sent before the consumer was started.

### Conclusion
- The consumer can read records from the beginning of the topic, which can lead to duplicate processing of records if not handled properly.

## E01-01: Read from 200

### Parameters and Process
- **Lab:** 02 Part2 Eye-tracking main.
- **Action:** Adjust the consumer to read from offset 200 and run the lab as provided.

### Observations
- The consumer reads all records from offset 200. As the offset starts from 0, the consumer reads the 201st record in the topic.

### Conclusion
- The consumer can read records from the specified offset, which can lead to not processing records that were sent before the consumer was started.
- If the consumer is not aware of the offset, it can lead to data loss.
