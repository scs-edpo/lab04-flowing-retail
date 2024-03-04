# Alias Configuration for Kafka

For Windows, you can set up your PowerShell Profile as outlined below to create aliases for the Kafka commands.

1. Open PowerShell
2. Run the following command to open your PowerShell profile in Notepad and create it if it doesn't exist:
    ```bash
    if (-not (Test-Path $PROFILE)) { New-Item -ItemType File -Path $PROFILE -Force }
   notepad $PROFILE
    ```
3. Copy and paste the following code into the file and save it:
   
   ```bash
   # Create Kafka Topic
   function Create-KafkaTopic {
       param(
           [string]$replicationFactor,
           [string]$partitions,
           [string]$topic
       )
       docker-compose exec kafka /opt/bitnami/kafka/bin/kafka-topics.sh --create --bootstrap-server 'kafka:9092' --replication-factor $replicationFactor --partitions $partitions --topic $topic
   }
   Set-Alias -Name create-topic -Value Create-KafkaTopic
   
   # List Kafka Topics
   function List-KafkaTopics {
       docker-compose exec kafka /opt/bitnami/kafka/bin/kafka-topics.sh --list --bootstrap-server 'kafka:9092'
   }
   Set-Alias -Name list-topics -Value List-KafkaTopics
   
   # Kafka Console Producer
   function Kafka-ConsoleProducer {
       param(
           [string]$topic
       )
       docker-compose exec kafka /opt/bitnami/kafka/bin/kafka-console-producer.sh --broker-list 'kafka:9092' --topic $topic
   }
   Set-Alias -Name kafka-producer -Value Kafka-ConsoleProducer
   
   # Kafka Console Consumer
   function Kafka-ConsoleConsumer {
       param(
           [string]$topic
       )
       docker-compose exec kafka /opt/bitnami/kafka/bin/kafka-console-consumer.sh --bootstrap-server 'kafka:9092' --topic $topic --from-beginning
   }
   Set-Alias -Name kafka-consumer -Value Kafka-ConsoleConsumer
   ```

4. After adding the functions and aliases to your profile, save the file and apply the changes by either restarting PowerShell or sourcing your profile with 
    ```bash
    . $PROFILE
    ```

5. Using the aliases by running the following commands in PowerShell:

* To create a topic with a specified replication factor, number of partitions, and topic name:
    ```bash
    create-topic -replicationFactor 1 -partitions 1 -topic helloworld
    ```
* To list all topics:
    ```bash
    list-topics
    ```
* To start a Kafka producer for a specific topic:
    ```bash
    kafka-producer -topic helloworld
    ```
* To start a Kafka consumer for a specific topic from the beginning:
    ```bash
    kafka-consumer -topic helloworld
    ```
