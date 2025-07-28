package com.sosohandays.metaadmin.co.jwt;

import com.sosohandays.metaadmin.co.admacctmngt.biz.COJwtAdmAcctTokenBIZ;
import com.sosohandays.metaadmin.co.admacctmngt.dto.COJwtAdmAcctTokenDTO;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final COJwtAdmAcctTokenBIZ tokenBiz;

    /**
     * JWT 토큰 인증 필터의 핵심 메서드
     * - 토큰이 유효하면 Spring Security Context에 인증 정보 설정
     * - 토큰이 무효하면 AuthenticationEntryPoint에서 처리하도록 위임
     */
    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain) throws ServletException, IOException {

        // 1. 토큰 추출
        String token = jwtUtil.extractTokenFromRequest(request);
        if (token == null) {
            // 토큰이 없으면 인증 없이 다음 필터로 진행
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 2. 토큰 검증 및 인증 처리
            if (validateTokenAndSetAuthentication(token, request)) {
                log.debug("JWT 토큰 인증 성공: {}", jwtUtil.getUsernameFromAccessToken(token));
            } else {
                log.debug("JWT 토큰 검증 실패");
                // 인증 실패 시에도 다음 필터로 진행 -> AuthenticationEntryPoint에서 처리
            }

        } catch (ExpiredJwtException e) {
            log.debug("JWT 토큰 만료: {}", e.getMessage());
            // 만료된 토큰도 AuthenticationEntryPoint에서 처리
        } catch (Exception e) {
            log.warn("JWT 토큰 처리 중 오류: {}", e.getMessage());
            // 기타 오류도 AuthenticationEntryPoint에서 처리
        }

        // 인증 성공/실패 관계없이 다음 필터로 진행
        // 인증이 실패한 경우 Spring Security가 AuthenticationEntryPoint를 호출함
        filterChain.doFilter(request, response);
    }

    /**
     * 토큰 검증 및 인증 정보 설정
     *
     * @param token JWT 토큰
     * @param request HTTP 요청
     * @return true: 인증 성공, false: 인증 실패
     */
    private boolean validateTokenAndSetAuthentication(String token, HttpServletRequest request) {
        try {
            // 1. 토큰 형식 및 서명 검증
            if (!jwtUtil.validateAccessToken(token)) {
                return false;
            }

            // 2. 토큰에서 사용자 정보 추출
            String username = jwtUtil.getUsernameFromAccessToken(token);
            String devId = jwtUtil.extractDeviceIdFromRequest(request);

            // 3. DB에서 토큰 유효성 확인
            if (!isTokenValidInDatabase(username, devId)) {
                return false;
            }

            // 4. 인증 성공 - Spring Security Context에 설정
            setAuthentication(username);
            return true;

        } catch (Exception e) {
            log.debug("토큰 검증 실패: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 데이터베이스에서 토큰 유효성 검증
     * - 로그아웃된 토큰인지 확인
     * - 해당 사용자/기기 조합이 DB에 존재하는지 확인
     * - 리프레시 토큰이 있는지 확인 (세션이 유효한지)
     *
     * @param username 사용자 ID
     * @param devId 기기 ID
     * @return true: 유효한 토큰, false: 무효한 토큰
     */
    private boolean isTokenValidInDatabase(String username, String devId) {
        try {
            COJwtAdmAcctTokenDTO dto = new COJwtAdmAcctTokenDTO();
            dto.setAcctId(username);
            dto.setDevId(devId);

            COJwtAdmAcctTokenDTO dbToken = tokenBiz.getByAcctIdAndDevId(dto);

            // DB에 토큰이 없거나, 저장된 리프레시 토큰과 연관성이 없으면 무효
            return dbToken != null && dbToken.getRefTkn() != null;

        } catch (Exception e) {
            log.warn("DB 토큰 검증 중 오류 발생: {}", e.getMessage());
            return false; // 오류 시 토큰 무효로 처리
        }
    }

    /**
     * Spring Security Context에 인증 정보 설정
     * - 인증된 사용자 정보를 Spring Security에 등록
     * - 이후 컨트롤러에서 @AuthenticationPrincipal로 사용자 정보 접근 가능
     * - 권한 정보도 함께 설정 (현재는 ROLE_USER 고정)
     *
     * @param username 인증된 사용자 ID
     */
    private void setAuthentication(String username) {
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}