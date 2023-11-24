package com.example.newsservice.service;

import com.example.newsservice.model.dto.pagination.PageParameter;
import com.example.newsservice.model.entity.User;

import java.util.List;

public interface UserService {

    List<User> findUsers(PageParameter pageParameter);
    User findById(Long id);

    User saveUser(User user);
    User updateUser(User user);

    void deleteById(Long id);
}
