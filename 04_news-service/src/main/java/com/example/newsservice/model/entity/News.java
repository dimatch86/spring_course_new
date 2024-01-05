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
@Table(name = "news")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;

    @CreationTimestamp
    @Column(name = "create_at")
    private Instant createAt;
    @UpdateTimestamp
    @Column(name = "update_at")
    private Instant updateAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Comment> commentList;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "category_to_news",
            joinColumns = {@JoinColumn(name = "news_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id", referencedColumnName = "id")})
    private List<Category> categories = new ArrayList<>();
}
