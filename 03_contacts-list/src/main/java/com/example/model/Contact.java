package com.example.model;

import com.example.util.ValidationUtil;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@Data
@FieldNameConstants
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Contact {

    private Long id;
    @Pattern(regexp = ValidationUtil.FIRST_NAME_PATTERN)
    private String firstName;
    @Pattern(regexp = ValidationUtil.LAST_NAME_PATTERN)
    private String lastName;
    @Pattern(regexp = ValidationUtil.EMAIL_PATTERN)
    private String email;
    @Pattern(regexp = ValidationUtil.PHONE_NUMBER_PATTERN)
    private String phone;
}
