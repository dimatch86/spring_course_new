package org.example.mapper;

import lombok.experimental.UtilityClass;
import org.example.OrderEvent;
import org.example.model.Order;

@UtilityClass
public class OrderEventMapper {

    public OrderEvent orderToOrderEvent(Order order) {
        return OrderEvent.builder()
                .product(order.getProduct())
                .quantity(order.getQuantity())
                .build();
    }
}
