package com.example.newsservice.mapstruct;

import com.example.newsservice.model.dto.UserListResponse;
import com.example.newsservice.model.dto.UserResponse;
import com.example.newsservice.model.entity.User;

import java.util.List;

public abstract class UserMapperDelegate implements UserMapper {

    @Override
    public UserListResponse userListToUserResponseList(List<User> users) {
        List<UserResponse> responseList = users.stream().map(user -> UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .newsCount(user.getNewsList().size())
                .build()).toList();
        return new UserListResponse(responseList);
    }
}
