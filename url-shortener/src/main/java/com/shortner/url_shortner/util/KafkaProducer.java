package com.shortner.url_shortner.util;

import com.shortner.url_shortner.event.AccessEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class KafkaProducer {
    public static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

    @Autowired
    private KafkaTemplate<String, AccessEvent> kafkaTemplate;

    @Async
    public void send(String topic, String key, AccessEvent accessEvent) {
        logger.info("Publishing event to kafka: topic = {} , key = {}, event = {}", topic, key, accessEvent);
        CompletableFuture<SendResult<String, AccessEvent>> future = kafkaTemplate.send(topic, key, accessEvent);
        future.whenComplete((result,exception)->{
            if (exception != null) {
                logger.error("Failed to send event", exception);
            } else {
                logger.info("Event sent to kafka : {}" , result.getRecordMetadata());
            }
        });
    }
}
