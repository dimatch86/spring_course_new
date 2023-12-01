package org.example;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderEvent {
    private String product;
    private Integer quantity;
}
