package com.project.BloggingApp.security.spring_security;

import com.project.BloggingApp.security.jwt.JwtFilter;
import com.project.BloggingApp.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(request -> request
                .requestMatchers("/public/**").permitAll()
                .requestMatchers("/users/**").authenticated()
                .requestMatchers("/articles/**").authenticated()
                .requestMatchers("/swagger-ui/**").permitAll()
        ).csrf(AbstractHttpConfigurer::disable)
         .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
         .build();
    }

    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
        return auth.getAuthenticationManager();
    }
}
