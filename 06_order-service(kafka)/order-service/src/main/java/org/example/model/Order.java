package org.example.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class Order {

    @NotBlank(message = "Поле product должно быть указано")
    private String product;

    @NotNull(message = "Поле quantity должно быть указано")
    private Integer quantity;
}
