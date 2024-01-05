package com.example.newsservice.configuration;

import com.example.newsservice.model.entity.RoleType;
import com.example.newsservice.utils.UrlUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig implements WebMvcConfigurer {

    private static final String USER = RoleType.ROLE_USER.getRole();
    private static final String ADMIN = RoleType.ROLE_ADMIN.getRole();
    private static final String MODERATOR = RoleType.ROLE_MODERATOR.getRole();

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       UserDetailsService userDetailsService,
                                                       PasswordEncoder passwordEncoder) throws Exception {

        var authManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authManagerBuilder.userDetailsService(userDetailsService);
        var authProvider = new DaoAuthenticationProvider(passwordEncoder);
        authProvider.setUserDetailsService(userDetailsService);
        authManagerBuilder.authenticationProvider(authProvider);
        return authManagerBuilder.build();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("*").allowedHeaders("*");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(form -> form.loginPage(UrlUtil.SIGN_IN).permitAll().failureUrl(UrlUtil.SIGN_IN))
                .authorizeHttpRequests(auth -> auth.requestMatchers("/").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                        .requestMatchers(HttpMethod.GET, UrlUtil.REGISTRATION_URL).permitAll()
                        .requestMatchers(HttpMethod.POST, UrlUtil.BASIC_USER_URL).permitAll()
                        .requestMatchers(HttpMethod.GET, UrlUtil.BASIC_USER_URL).hasRole(ADMIN)
                        .requestMatchers(HttpMethod.GET, UrlUtil.USER_ID_URL).hasAnyRole(USER, ADMIN, MODERATOR)
                        .requestMatchers(HttpMethod.POST, UrlUtil.USER_ID_URL).hasAnyRole(USER, ADMIN, MODERATOR)
                        .requestMatchers(HttpMethod.DELETE, UrlUtil.USER_ID_URL).hasAnyRole(USER, ADMIN, MODERATOR)
                        .requestMatchers(HttpMethod.POST, UrlUtil.ADD_ROLE_URL).hasRole(ADMIN)
                        .requestMatchers(HttpMethod.GET, UrlUtil.BASIC_CATEGORY_URL).hasAnyRole(USER, ADMIN, MODERATOR)
                        .requestMatchers(HttpMethod.GET, UrlUtil.CATEGORY_ID_URL).hasAnyRole(USER, ADMIN, MODERATOR)
                        .requestMatchers(HttpMethod.POST, UrlUtil.BASIC_CATEGORY_URL).hasAnyRole(ADMIN, MODERATOR)
                        .requestMatchers(HttpMethod.POST, UrlUtil.CATEGORY_ID_URL).hasAnyRole(ADMIN, MODERATOR)
                        .requestMatchers(HttpMethod.DELETE, UrlUtil.CATEGORY_ID_URL).hasAnyRole(ADMIN, MODERATOR)
                        .requestMatchers(HttpMethod.GET, UrlUtil.BASIC_NEWS_URL).hasAnyRole(USER, ADMIN, MODERATOR)
                        .requestMatchers(HttpMethod.GET, UrlUtil.NEWS_ID_URL).hasAnyRole(USER, ADMIN, MODERATOR)
                        .requestMatchers(HttpMethod.POST, UrlUtil.BASIC_NEWS_URL).hasAnyRole(USER, ADMIN, MODERATOR)
                        .requestMatchers(HttpMethod.POST, UrlUtil.NEWS_ID_URL).hasAnyRole(USER, ADMIN, MODERATOR)
                        .requestMatchers(HttpMethod.DELETE, UrlUtil.NEWS_ID_URL).hasAnyRole(USER, ADMIN, MODERATOR)
                        .requestMatchers(UrlUtil.COMMENT_NEWS_ID_URL).hasAnyRole(USER, ADMIN, MODERATOR)
                        .requestMatchers(HttpMethod.POST, UrlUtil.BASIC_COMMENT_URL).hasAnyRole(USER, ADMIN, MODERATOR)
                        .requestMatchers(HttpMethod.POST, UrlUtil.COMMENT_ID_URL).hasAnyRole(USER, ADMIN, MODERATOR)
                        .requestMatchers(HttpMethod.DELETE, UrlUtil.COMMENT_ID_URL).hasAnyRole(USER, ADMIN, MODERATOR)
                .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationManager(authenticationManager);

        return http.build();
    }
}
