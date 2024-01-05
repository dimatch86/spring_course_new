package com.example.newsservice.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UrlUtil {

    public final String BASIC_USER_URL = "/api/v1/user";
    public final String USER_ID_URL = "/api/v1/user/{id}";
    public final String BASIC_NEWS_URL = "/api/v1/news";
    public final String NEWS_ID_URL = "/api/v1/news/{id}";
    public final String BASIC_COMMENT_URL = "/api/v1/comment";
    public final String COMMENT_NEWS_ID_URL = "/api/v1/comment/{id}";
    public final String COMMENT_ID_URL = "/api/v1/comment/{id}";
    public final String BASIC_CATEGORY_URL = "/api/v1/category";
    public final String CATEGORY_ID_URL = "/api/v1/category/{id}";
    public final String SIGN_IN = "/login";
    public final String REGISTRATION_URL = "/registration";
    public final String ADD_ROLE_URL = "/api/v1/user/role/{id}";

}
