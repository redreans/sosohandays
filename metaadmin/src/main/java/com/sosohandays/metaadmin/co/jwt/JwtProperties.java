package com.sosohandays.metaadmin.co.jwt;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jwt") // application.yml의 'jwt' 접두사를 가진 속성들을 주입받음
public class JwtProperties {
    private String accessSecret;
    private long accessExpirationMs;
    private String refreshSecret;
    private long refreshExpirationMs;
}