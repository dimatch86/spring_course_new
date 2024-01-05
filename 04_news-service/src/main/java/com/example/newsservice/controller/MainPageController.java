package com.example.newsservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainPageController {

    @GetMapping("/")
    public String getMainPage() {
        return "index";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login-page";
    }

    @GetMapping("/registration")
    public String getRegistrationPage() {
        return "registration";
    }
}
