package org.example.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.OrderEvent;
import org.example.OrderStatusEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderStatusListener {

    @Value("${app.kafka.kafkaMessageTopic}")
    private String topicToOrderService;

    private final KafkaTemplate<String, OrderStatusEvent> kafkaTemplate;
    private final List<String> statuses = List.of("CREATED", "PROCESS", "FAILED");

    @KafkaListener(topics = "${app.kafka.kafkaListeningTopic}",
            groupId = "${app.kafka.kafkaMessageGroupId}",
            containerFactory = "kafkaListenerOrderContainerFactory")
    public void listen(@Payload OrderEvent orderEvent,
                       @Header(value = KafkaHeaders.RECEIVED_MESSAGE_KEY, required = false) UUID key,
                       @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                       @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Integer partition,
                       @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long timestamp) {

        log.info("Received order: {}", orderEvent);
        log.info("Key: {}; Partition: {}; Topic: {}; Timestamp: {}", key, partition, topic, timestamp);

        int randomIndex = new Random().nextInt(statuses.size());
        String status = statuses.get(randomIndex);
        OrderStatusEvent orderStatusEvent = OrderStatusEvent.builder()
                .status(status)
                .date(Instant.now())
                .build();

        kafkaTemplate.send(topicToOrderService, orderStatusEvent);
        log.info("Order status {} sent to order-service", status);
    }
}
