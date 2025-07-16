// jwt/JwtUtil.java
package com.sosohandays.metaadmin.co.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys; // Keys.hmacShaKeyFor를 위해 필요
import jakarta.annotation.PostConstruct; // @PostConstruct를 위해 필요
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // 로그를 위해 추가
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Slf4j // 로그 사용을 위해 추가
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtProperties jwtProperties;

    // 시크릿 키는 JwtProperties에서 주입받고, Key 객체는 @PostConstruct에서 초기화
    private Key accessKey;
    private Key refreshKey;

    // JwtProperties에서 주입받은 시크릿 키를 기반으로 Key 객체 초기화
    @PostConstruct
    public void init() {
        this.accessKey = Keys.hmacShaKeyFor(jwtProperties.getAccessSecret().getBytes());
        this.refreshKey = Keys.hmacShaKeyFor(jwtProperties.getRefreshSecret().getBytes());
    }

    // 액세스 토큰 생성
    public String generateAccessToken(String username) {
        return generateToken(username, accessKey, jwtProperties.getAccessExpirationMs());
    }

    // 리프레시 토큰 생성
    public String generateRefreshToken(String username) {
        return generateToken(username, refreshKey, jwtProperties.getRefreshExpirationMs());
    }

    private String generateToken(String username, Key key, long expiration) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Date getExpirationDateFromToken(String token, boolean isRefreshToken) {
        Key key = isRefreshToken ? refreshKey : accessKey;
        Claims claims = parseClaims(token, key);
        return claims.getExpiration();
    }

    public boolean validateAccessToken(String token) {
        return validateToken(token, false);
    }

    public boolean validateRefreshToken(String token) {
        return validateToken(token, true);
    }

    public boolean validateToken(String token, boolean isRefreshToken) {
        Key key = isRefreshToken ? refreshKey : accessKey;
        try {
            Jwts.parser().verifyWith((SecretKey)key).build().parseSignedClaims(token);

            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.", e);
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.", e);
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.", e);
        }
        return false;
    }

    public String getUsernameFromAccessToken(String token) {
        return getUsernameFromToken(token, false);
    }

    public String getUsernameFromRefreshToken(String token) {
        return getUsernameFromToken(token, true);
    }

    public String getUsernameFromToken(String token, boolean isRefreshToken) {
        Key key = isRefreshToken ? refreshKey : accessKey;
        Claims claims = parseClaims(token, key);
        return claims.getSubject();
    }

    // isAccessToken 파라미터가 아닌, isRefreshToken 파라미터를 그대로 사용하도록 수정
    public boolean isTokenExpired(String token, boolean isRefreshToken) { // isAccessToken -> isRefreshToken으로 변경
        try {
            Key key = isRefreshToken ? refreshKey : accessKey; // isRefreshToken이 true면 리프레시 키, 아니면 액세스 키
            Date expiration = parseClaims(token, key).getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            log.warn("토큰 만료일 검증 중 오류 발생: {}", e.getMessage());
            return true; // 예외 발생 시 토큰 만료로 간주
        }
    }

    private Claims parseClaims(String token, Key key) {
        try {
            // jjwt 0.12.x API 변경: parser().verifyWith(key).build().parseSignedClaims().getPayload()
            // parseClaimsJws() 대신 parseSignedClaims() 사용, getBody() 대신 getPayload() 사용
            return Jwts.parser()
                    .verifyWith((SecretKey)key) // 키 설정 방식 변경
                    .build()
                    .parseSignedClaims(token) // 메서드명 변경
                    .getPayload(); // 페이로드 접근 메서드명 변경
        } catch (ExpiredJwtException e) {
            // 만료된 토큰에서도 클레임 정보는 추출 가능 (주로 리프레시 토큰 재발급 시 사용)
            return e.getClaims();
        }
    }
}