package com.sosohandays.metaadmin.co.config;

import com.sosohandays.metaadmin.co.admacctmngt.biz.COJwtAdmAcctTokenBIZ;
import com.sosohandays.metaadmin.co.jwt.JwtAuthenticationEntryPoint;
import com.sosohandays.metaadmin.co.jwt.JwtAuthenticationFilter;
import com.sosohandays.metaadmin.co.jwt.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtUtil jwtUtil, COJwtAdmAcctTokenBIZ coJwtAdmAcctTokenBIZ) {
        return new JwtAuthenticationFilter(jwtUtil, coJwtAdmAcctTokenBIZ);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           JwtAuthenticationFilter jwtAuthenticationFilter,
                                           JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/co/admacctmngt/login"
                                           , "/co/admacctmngt/logout"
                                           , "/co/admacctmngt/insert"
                                           , "/error"
                                           , "/actuator/**"     // 헬스체크 등
                                           , "/favicon.ico").permitAll()
                                .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}