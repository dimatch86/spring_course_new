package com.example.newsservice.filter;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

@Data
@FieldNameConstants
public class NewsFilter {

    private String author;
    private String category;
}
