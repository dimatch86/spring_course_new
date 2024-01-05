package com.example.newsservice.service;

import com.example.newsservice.aop.Authorizable;
import com.example.newsservice.exception.EntityNotFoundException;
import com.example.newsservice.model.dto.pagination.PageParameter;
import com.example.newsservice.model.entity.Role;
import com.example.newsservice.model.entity.RoleType;
import com.example.newsservice.model.entity.User;
import com.example.newsservice.repository.RoleRepository;
import com.example.newsservice.repository.UserRepository;
import com.example.newsservice.utils.BeanUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> findUsers(PageParameter pageParameter) {
        return userRepository.findAll(PageRequest.of(pageParameter.getPageNumber(), pageParameter.getPageSize()))
                .getContent();
    }

    @Authorizable
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("User with id {0} not found", id)));
    }

    @Override
    @Transactional
    public User saveUser(User user, Role role) {
        Role roleForUser = roleRepository.findRoleByAuthority(role.getAuthority()).orElse(role);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(roleForUser));
        return userRepository.save(user);
    }

    @Override
    @Transactional
    @Authorizable
    public User updateUser(User user, Role role) {
        User existedUser = userRepository.findById(user.getId()).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("User with id {0} not found", user.getId())));
        Role roleForUser = roleRepository.findRoleByAuthority(role.getAuthority()).orElse(role);

        BeanUtil.copyNonNullProperties(user, existedUser);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(roleForUser));
        return userRepository.save(user);
    }

    @Override
    public User findUserByName(String name) {
        return userRepository.findUserByName(name).orElseThrow(() ->
                new EntityNotFoundException("User not found"));
    }

    @Override
    @Transactional
    @Authorizable
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public User addRole(Long id, RoleType roleType) {
        User user = userRepository.findById(id).orElseThrow();
        Role role = roleRepository.findRoleByAuthority(roleType).orElse(Role.from(roleType));
        user.getRoles().add(role);
        return userRepository.save(user);
    }
}
