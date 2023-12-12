package com.example.tasktracker.entity;

public enum TaskStatus {
    TODO, IN_PROGRESS, DONE;

    public static TaskStatus getTaskStatus(String str) {
        return switch (str) {
            case "IN_PROGRESS" -> TaskStatus.IN_PROGRESS;
            case "DONE" -> TaskStatus.DONE;
            default -> TaskStatus.TODO;
        };
    }
}
