package com.example.studentsaccounting.listener;

import com.example.studentsaccounting.event.AllStudentsDeletionEvent;
import com.example.studentsaccounting.event.StudentCreationEvent;
import com.example.studentsaccounting.event.StudentDeletionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class EventHolderListener {
    @EventListener(StudentCreationEvent.class)
    public void listen(StudentCreationEvent studentCreationEvent) {
        log.info("Student {} created", studentCreationEvent.getStudent());
    }
    @EventListener(StudentDeletionEvent.class)
    public void listen(StudentDeletionEvent studentDeletionEvent) {
        log.info("Student with id {} removed", studentDeletionEvent.getId());
    }
    @EventListener(AllStudentsDeletionEvent.class)
    public void listen() {
        log.info("All students deleted");
    }
}
