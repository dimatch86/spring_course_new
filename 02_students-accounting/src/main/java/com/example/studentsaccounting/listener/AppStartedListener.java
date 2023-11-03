package com.example.studentsaccounting.listener;

import com.example.studentsaccounting.service.StudentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@ConditionalOnProperty("app.creation.enabled")
@RequiredArgsConstructor
@Component
public class AppStartedListener {

    @Value("${app.path}")
    private String path;

    private final StudentsService service;

    @EventListener(ApplicationStartedEvent.class)
    public void loadStudents() {
        try(BufferedReader br = new BufferedReader (new FileReader(path))) {
            String student;
            while((student=br.readLine()) != null) {

                String[] studentDetails = student.split(" ");
                if (studentDetails.length != 3) {
                    continue;
                }
                service.addStudent(studentDetails[1], studentDetails[0], Integer.parseInt(studentDetails[2]));
            }
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }
}
