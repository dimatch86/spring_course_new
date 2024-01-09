package com.example.tasktracker.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UrlUtil {

    public final String SIGN_IN = "/login";
    public final String REGISTRATION_URL = "/registration";
    public final String BASIC_USER_URL = "/api/v1/users";
    public final String USER_ID_URL = "/api/v1/users/{id}";
    public final String BASIC_TASK_URL = "/api/v1/tasks";
    public final String TASK_ID_URL = "/api/v1/tasks/{id}";
    public final String ADD_OBSERVER_URL = "/api/v1/tasks/observer/{id}";
    public final String STREAM_URL = "/api/v1/tasks/stream";

}
