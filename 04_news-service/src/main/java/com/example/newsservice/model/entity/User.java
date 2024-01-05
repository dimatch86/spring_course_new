package com.example.newsservice.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "`user`")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
@EqualsAndHashCode
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true)
    private String name;

    @NotNull
    @NotBlank
    @Column(name = "password")
    private String password;

    @CreationTimestamp
    @Column(name = "create_at")
    private Instant createAt;
    @UpdateTimestamp
    @Column(name = "update_at")
    private Instant updateAt;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<News> newsList = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "user_to_authorities",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_id")})

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    private Set<Role> roles = new HashSet<>();
}
