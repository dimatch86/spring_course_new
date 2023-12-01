package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.OrderEvent;
import org.example.OrderStatusEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderMessageService {

    @Value("${app.kafka.kafkaMessageTopic}")
    private String topicName;
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    private final List<OrderStatusEvent> statusEvents = new ArrayList<>();

    public String sendMessage(OrderEvent orderEvent) {
        kafkaTemplate.send(topicName, orderEvent);
        return MessageFormat.format("OrderEvent with product {0} sent to order-status-service", orderEvent.getProduct());
    }

    public void add(OrderStatusEvent statusEvent) {
        statusEvents.add(statusEvent);
    }

    public Optional<OrderStatusEvent> getByStatusName(String statusName) {
        return statusEvents.stream().filter(event -> event.getStatus().equals(statusName)).findFirst();
    }

    public List<OrderStatusEvent> getOrderStatusEventList() {
        return statusEvents;
    }
}
