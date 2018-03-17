package com.ipponusa;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

public class starting_consumer {

    public static void main(String[] args) {

        Properties props = new Properties();
        // host and port to Kafka server
        props.put("bootstrap.servers", "localhost:9092");
        // specify group.id prop, tells Kafka which consumer group this consumer belongs to
        // principle is consumers w/in group each receive subset of the messages being published, sharing the load
        props.put("group.id", "mygroup");
        // specify key.deserializer and value.deserializer using StringDeserializer class
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        // create a consumer instance
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        // subscribe to 1 or more topics, using subscribe()
        consumer.subscribe(Arrays.asList("mytopic"));

        boolean running = true;
        while (running) {
            // query for messages using poll(100ms = timeout timer)
            // returns ConsumerRecords which is list of ConsumerRecord objects
            // each object is a message which eky + value are deserialized using classes in properties
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.println(record.value());
            }
        }

        consumer.close();
    }
}
