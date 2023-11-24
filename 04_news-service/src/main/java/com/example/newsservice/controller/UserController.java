package com.example.newsservice.controller;

import com.example.newsservice.mapstruct.UserMapper;
import com.example.newsservice.model.dto.UpsertUserRequest;
import com.example.newsservice.model.dto.UserListResponse;
import com.example.newsservice.model.dto.UserResponse;
import com.example.newsservice.model.dto.pagination.PageParameter;
import com.example.newsservice.model.entity.User;
import com.example.newsservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<UserListResponse> findAll(@Valid PageParameter pageParameter) {
        return ResponseEntity.ok(
                userMapper.userListToUserResponseList(userService.findUsers(pageParameter))
        );
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userMapper.userToResponse(userService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UpsertUserRequest request) {
        User user = userService.saveUser(userMapper.requestToUser(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.userToResponse(user));
    }

    @PostMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Long id, @Valid @RequestBody UpsertUserRequest request) {
        User user = userService.updateUser(userMapper.requestToUser(id, request));
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.userToResponse(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
