package com.example.newsservice.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "authorities")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Enumerated(value = EnumType.STRING)
    private RoleType authority;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    Set<User> users = new HashSet<>();

    public GrantedAuthority toAuthority() {
        return new SimpleGrantedAuthority(authority.name());
    }

    public static Role from(RoleType type) {
        var role = new Role();
        role.setAuthority(type);
        return role;
    }
}
