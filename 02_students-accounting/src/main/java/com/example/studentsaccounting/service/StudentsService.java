package com.example.studentsaccounting.service;

import com.example.studentsaccounting.event.AllStudentsDeletionEvent;
import com.example.studentsaccounting.event.StudentCreationEvent;
import com.example.studentsaccounting.event.StudentDeletionEvent;
import com.example.studentsaccounting.model.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class StudentsService {

    private int generatedId;
    private final List<Student> students;
    private final ApplicationEventPublisher publisher;

    @ShellMethod(key = "add")
    public void addStudent(String firstName, String lastName, int age) {
        Student student = new Student(++generatedId, firstName, lastName, age);
        students.add(student);
        publisher.publishEvent(new StudentCreationEvent(this, student));
    }

    @ShellMethod(key = "list")
    public void listStudents() {
        students.forEach(System.out::println);
    }

    @ShellMethod(key = "del")
    public void deleteStudent(int id) {

        students.stream()
                .filter(student -> student.getId() == id)
                .findFirst()
                .ifPresent(student -> {
                    students.remove(student);
                    publisher.publishEvent(new StudentDeletionEvent(this, student.getId()));
                });
    }

    @ShellMethod(key = "all")
    public void deleteAllStudents() {
        students.clear();
        publisher.publishEvent(new AllStudentsDeletionEvent(this));
    }
}
