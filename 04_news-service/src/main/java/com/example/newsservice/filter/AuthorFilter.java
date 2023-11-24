package com.example.newsservice.filter;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

@Data
@FieldNameConstants
public class AuthorFilter {

    @NotNull(message = "Пустой authorId!")
    private Integer authorId;
}
