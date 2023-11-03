package com.example.studentsaccounting.event;

import com.example.studentsaccounting.model.Student;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class StudentCreationEvent extends ApplicationEvent {

    private final Student student;

    public StudentCreationEvent(Object source, Student student) {
        super(source);
        this.student = student;
    }
}
