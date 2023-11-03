package com.example.studentsaccounting.event;

import org.springframework.context.ApplicationEvent;

public class AllStudentsDeletionEvent extends ApplicationEvent {

    public AllStudentsDeletionEvent(Object source) {
        super(source);
    }
}
