package com.example.tasktracker.service;

import com.example.tasktracker.entity.User;
import com.example.tasktracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ReactiveMongoTemplate mongoTemplate;

    public Flux<User> findAll() {
        return userRepository.findAll();
    }
    public Mono<Set<User>> findAllById(Set<String> ids) {
        return mongoTemplate.find(Query.query(Criteria.where("id").in(ids)), User.class)
                .collectList().map(HashSet::new);
    }

    public Mono<User> findById(String id) {
        return userRepository.findById(id);
    }

    public Mono<User> findByName(String name) {
        return userRepository.findUserByUserName(name);
    }

    public Mono<User> saveUser(User user) {
        user.setId(UUID.randomUUID().toString());
        return userRepository.save(user);
    }

    public Mono<User> update(String id, User user) {
        return findById(id).flatMap(userForUpdate -> {
            if (StringUtils.hasText(user.getUserName())) {
                userForUpdate.setUserName(user.getUserName());
            }
            if (StringUtils.hasText(user.getEmail())) {
                userForUpdate.setEmail(user.getEmail());
            }
            return userRepository.save(userForUpdate);
        });
    }

    public Mono<Void> deleteById(String id) {
        return userRepository.deleteById(id);
    }
}
