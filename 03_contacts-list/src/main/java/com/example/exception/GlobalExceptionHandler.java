package com.example.exception;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationExceptions(
            MethodArgumentNotValidException ex, RedirectAttributes redirectAttributes) {

        StringBuilder stringBuilder = new StringBuilder();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            stringBuilder.append(fieldName).append(": ").append(errorMessage).append(";\n");
        });
        redirectAttributes.addFlashAttribute("inputError", stringBuilder.toString());
        return "redirect:/contact/create";
    }
}
