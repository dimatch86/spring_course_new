package com.example.tasktracker.mapstruct;

import com.example.tasktracker.entity.RoleType;
import com.example.tasktracker.entity.User;
import com.example.tasktracker.model.UpsertUserRequest;
import com.example.tasktracker.model.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "roles", expression = "java(roleType.getRole() == null ? " +
            "java.util.Collections.emptySet() : " +
            "java.util.Set.of(roleType))")
    User requestToUser(UpsertUserRequest request, RoleType roleType);

    User requestToUser(UpsertUserRequest request);

    UserResponse userToResponse(User user);
}
