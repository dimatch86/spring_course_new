package org.example.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.OrderStatusEvent;
import org.example.service.OrderMessageService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderServiceListener {

    private final OrderMessageService orderMessageService;

    @KafkaListener(topics = "${app.kafka.kafkaListeningTopic}",
             groupId = "${app.kafka.kafkaMessageGroupId}",
            containerFactory = "kafkaListenerOrderContainerFactory")
    public void listen(@Payload OrderStatusEvent statusEvent,
                       @Header(value = KafkaHeaders.RECEIVED_MESSAGE_KEY, required = false) UUID key,
                       @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                       @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Integer partition,
                       @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long timestamp) {

        log.info("Received status: {}", statusEvent);
        log.info("Key: {}; Partition: {}; Topic: {}; Timestamp: {}", key, partition, topic, timestamp);

        orderMessageService.add(statusEvent);
    }
}
