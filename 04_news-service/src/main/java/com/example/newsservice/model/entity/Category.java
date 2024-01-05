package com.example.newsservice.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tag", unique = true)
    private String tag;

    @CreationTimestamp
    @Column(name = "create_at")
    private Instant createAt;
    @UpdateTimestamp
    @Column(name = "update_at")
    private Instant updateAt;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "categories")
    private List<News> newsList = new ArrayList<>();

    public Category(String tag) {
        this.tag = tag;
    }
}
