package com.example.newsservice.repository;

import com.example.newsservice.model.entity.Role;
import com.example.newsservice.model.entity.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findRoleByAuthority(RoleType roleType);
}
