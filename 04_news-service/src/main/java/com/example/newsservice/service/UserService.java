package com.example.newsservice.service;

import com.example.newsservice.model.dto.pagination.PageParameter;
import com.example.newsservice.model.entity.Role;
import com.example.newsservice.model.entity.RoleType;
import com.example.newsservice.model.entity.User;

import java.util.List;

public interface UserService {

    List<User> findUsers(PageParameter pageParameter);
    User findById(Long id);

    User saveUser(User user, Role role);
    User updateUser(User user, Role role);
    User findUserByName(String name);

    void deleteById(Long id);

    User addRole(Long id, RoleType roleType);
}
