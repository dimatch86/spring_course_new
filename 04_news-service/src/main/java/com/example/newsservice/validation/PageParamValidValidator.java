package com.example.newsservice.validation;

import com.example.newsservice.model.dto.pagination.PageParameter;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.ObjectUtils;

public class PageParamValidValidator implements ConstraintValidator<PageParamValid, PageParameter> {
    @Override
    public boolean isValid(PageParameter pageParameter, ConstraintValidatorContext constraintValidatorContext) {
        return !ObjectUtils.anyNull(pageParameter.getPageNumber(), pageParameter.getPageSize());
    }
}
