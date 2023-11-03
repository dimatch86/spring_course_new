package com.example.studentsaccounting.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class StudentDeletionEvent extends ApplicationEvent {

    private final int id;

    public StudentDeletionEvent(Object source, int id) {
        super(source);
        this.id = id;
    }
}
