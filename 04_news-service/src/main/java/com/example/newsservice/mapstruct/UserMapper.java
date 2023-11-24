package com.example.newsservice.mapstruct;

import com.example.newsservice.model.dto.UpsertUserRequest;
import com.example.newsservice.model.dto.UserListResponse;
import com.example.newsservice.model.dto.UserResponse;
import com.example.newsservice.model.entity.User;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {NewsMapper.class, CommentMapper.class})
@DecoratedWith(UserMapperDelegate.class)
public interface UserMapper {

    User requestToUser(UpsertUserRequest request);
    @Mapping(source = "userId", target = "id")
    User requestToUser(Long userId, UpsertUserRequest request);

    UserResponse userToResponse(User user);

    default UserListResponse userListToUserResponseList(List<User> users) {
        UserListResponse response = new UserListResponse();
        response.setUsers(users.stream().map(this::userToResponse).toList());
        return response;
    }


}
