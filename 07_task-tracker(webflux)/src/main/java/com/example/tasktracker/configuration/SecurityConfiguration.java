package com.example.tasktracker.configuration;

import com.example.tasktracker.entity.RoleType;
import com.example.tasktracker.util.UrlUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration {

    private static final String USER = RoleType.ROLE_USER.getRole();
    private static final String MANAGER = RoleType.ROLE_MANAGER.getRole();

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public ReactiveAuthenticationManager authenticationManager(ReactiveUserDetailsService userDetailsService,
                                                               PasswordEncoder passwordEncoder) {
        var reactiveAuthenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        reactiveAuthenticationManager.setPasswordEncoder(passwordEncoder);
        return reactiveAuthenticationManager;
    }

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http, ReactiveAuthenticationManager authenticationManager) {
        return buildDefaultHttpSecurity(http)
                .authenticationManager(authenticationManager)
                .build();
    }

    private ServerHttpSecurity buildDefaultHttpSecurity(ServerHttpSecurity http) {
        return http.csrf(ServerHttpSecurity.CsrfSpec::disable).formLogin(formLoginSpec -> formLoginSpec.loginPage(UrlUtil.SIGN_IN))

                .authorizeExchange(auth -> auth.pathMatchers("/").permitAll()
                        .pathMatchers(UrlUtil.SIGN_IN, UrlUtil.REGISTRATION_URL, UrlUtil.STREAM_URL).permitAll()
                        .pathMatchers("/css/**", "/js/**", "/img/**").permitAll()
                        .pathMatchers(HttpMethod.POST, UrlUtil.BASIC_USER_URL).permitAll()
                        .pathMatchers(HttpMethod.GET, UrlUtil.BASIC_USER_URL).hasAnyRole(USER, MANAGER)
                        .pathMatchers(HttpMethod.GET, UrlUtil.USER_ID_URL).hasAnyRole(USER, MANAGER)
                        .pathMatchers(HttpMethod.PUT, UrlUtil.USER_ID_URL).hasAnyRole(USER, MANAGER)
                        .pathMatchers(HttpMethod.DELETE, UrlUtil.USER_ID_URL).hasAnyRole(USER, MANAGER)
                        .pathMatchers(HttpMethod.GET, UrlUtil.BASIC_TASK_URL).hasAnyRole(USER, MANAGER)
                        .pathMatchers(HttpMethod.GET, UrlUtil.TASK_ID_URL).hasAnyRole(USER, MANAGER)
                        .pathMatchers(HttpMethod.PUT, UrlUtil.ADD_OBSERVER_URL).hasAnyRole(USER, MANAGER)
                        .pathMatchers(HttpMethod.POST, UrlUtil.BASIC_TASK_URL).hasAnyRole(MANAGER)
                        .pathMatchers(HttpMethod.PUT, UrlUtil.TASK_ID_URL).hasAnyRole(MANAGER)
                        .pathMatchers(HttpMethod.DELETE, UrlUtil.TASK_ID_URL).hasAnyRole(MANAGER)
                        .anyExchange().authenticated()).httpBasic(Customizer.withDefaults());
    }
}
