package org.example;

public class ValidationUtils {

    public static final String FULL_NAME_PATTERN = "^[a-zA-Zа-яА-Я]+([\\s-][a-zA-Zа-яА-Я]+){2}$";
    public static final String EMAIL_PATTERN = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    public static final String PHONE_NUMBER_PATTERN = "\\+89\\d{7}";
}
