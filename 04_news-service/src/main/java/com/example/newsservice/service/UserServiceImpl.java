package com.example.newsservice.service;

import com.example.newsservice.exception.EntityNotFoundException;
import com.example.newsservice.model.dto.pagination.PageParameter;
import com.example.newsservice.model.entity.User;
import com.example.newsservice.repository.UserRepository;
import com.example.newsservice.utils.BeanUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> findUsers(PageParameter pageParameter) {
        return userRepository.findAll(PageRequest.of(pageParameter.getPageNumber(), pageParameter.getPageSize()))
                .getContent();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("User with id {0} not found", id)));
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        User existedUser = userRepository.findById(user.getId()).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("User with id {0} not found", user.getId())));

        BeanUtil.copyNonNullProperties(user, existedUser);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
