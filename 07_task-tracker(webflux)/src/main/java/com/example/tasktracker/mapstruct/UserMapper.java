package com.example.tasktracker.mapstruct;

import com.example.tasktracker.entity.User;
import com.example.tasktracker.model.UpsertUserRequest;
import com.example.tasktracker.model.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User requestToUser(UpsertUserRequest request);

    UserResponse userToResponse(User user);
}
