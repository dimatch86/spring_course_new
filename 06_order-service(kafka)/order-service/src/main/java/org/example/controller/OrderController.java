package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.OrderStatusEvent;
import org.example.mapper.OrderEventMapper;
import org.example.model.Order;
import org.example.service.OrderMessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/kafka")
@RequiredArgsConstructor
public class OrderController {

    private final OrderMessageService orderMessageService;

    @PostMapping("/order")
    public ResponseEntity<String> sendOrder(@Valid @RequestBody Order order) {
        return ResponseEntity.ok(orderMessageService.sendMessage(OrderEventMapper.orderToOrderEvent(order)));

    }

    @GetMapping("/{name}")
    public ResponseEntity<OrderStatusEvent> getByStatusName(@PathVariable String name) {
        return ResponseEntity.ok(orderMessageService.getByStatusName(name).orElseThrow());
    }

    @GetMapping("/list")
    public ResponseEntity<List<OrderStatusEvent>> getOrderStatusEvents() {
        return ResponseEntity.ok(orderMessageService.getOrderStatusEventList());
    }
}
