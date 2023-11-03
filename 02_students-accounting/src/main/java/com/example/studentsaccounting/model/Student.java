package com.example.studentsaccounting.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.MessageFormat;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class Student {

    int id;
    private String firstName;
    private String lastName;
    private int age;

    @Override
    public String toString() {
        return MessageFormat.format("{0} {1} {2} {3}", id, firstName, lastName, age);
    }
}
