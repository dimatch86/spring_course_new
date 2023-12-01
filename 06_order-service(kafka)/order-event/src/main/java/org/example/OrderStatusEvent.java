package org.example;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class OrderStatusEvent {

    private String status;
    private Instant date;
}
