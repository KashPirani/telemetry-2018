package com.ipponusa;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class starting_producer {

    public static void main(String[] args) {
        // create a set of properties
        Properties props = new Properties();

        // host and port to Kafka server
        props.put("bootstrap.servers", "localhost:9092");
        // class to serialize key of messages (messages have key + value, key is optional but serializer is necessary)
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // class to serialize value of message (send strings -> StringSerializer)
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        // create instance of KafkaProducer, give key = string, value = string, and properties above
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        //send 100 messages
        for (int i = 0; i < 100; i++) {
            // messages are of type ProducerRecord(generic params), specify name of topic & value
            ProducerRecord<String, String> record = new ProducerRecord<>("mytopic", "value-" + i);
            // send message using send method of KafkaProducer
            producer.send(record);
        }
        // shut it down to release resources
        producer.close();
    }
}
