package com.example.newsservice.model.dto.pagination;

import com.example.newsservice.validation.PageParamValid;
import lombok.Data;

@Data
@PageParamValid
public class PageParameter {
    private Integer pageSize;
    private Integer pageNumber;
}
