package com.sosohandays.metaadmin.co.admacctmngt.biz;

import com.sosohandays.common.util.DateUtil;
import com.sosohandays.metaadmin.co.admacctmngt.dto.COAdmAcctMngtDTO;
import com.sosohandays.metaadmin.co.admacctmngt.dto.COAdmAcctMngtSub01DTO;
import com.sosohandays.common.exception.SshdException;
import com.sosohandays.metaadmin.co.admacctmngt.dto.COJwtAdmAcctTokenDTO;
import com.sosohandays.metaadmin.co.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sosohandays.common.response.SshdResponse;
import com.sosohandays.common.util.MapUtil;
import com.sosohandays.common.util.StringUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 관리자 계정 관리 서비스 클래스
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class COAdmAcctMngtSVC {

    private final COAdmAcctMngtMapper cOAdmAcctMngtMapper;
    private final PasswordEncoder passwordEncoder;
    private final COJwtAdmAcctTokenBIZ cOJwtAdmAcctTokenBIZ;
    private final JwtUtil jwtUtil;

    @Transactional
    public SshdResponse<COJwtAdmAcctTokenDTO> login(COAdmAcctMngtDTO cOAdmAcctMngtDTO) {

        String acctId   = cOAdmAcctMngtDTO.getAcctId();
        String inputPwd = cOAdmAcctMngtDTO.getAcctPwd();
        String devId    = cOAdmAcctMngtDTO.getDevId();

        if (StringUtil.isNullOrEmpty(acctId) || StringUtil.isNullOrEmpty(inputPwd)) {
            throw new SshdException("아이디 또는 비밀번호가 누락되었습니다.");
        }

        // 1. 사용자 조회
        List<COAdmAcctMngtSub01DTO> resultList = cOAdmAcctMngtMapper.selectCOAdmAcmtById(cOAdmAcctMngtDTO);

        if (resultList == null || resultList.isEmpty()) {
            throw new SshdException("존재하지 않는 계정입니다.");
        }

        // 2. 사용 여부 확인
        if (!"Y".equalsIgnoreCase(resultList.getFirst().getUseYn())) {
            throw new SshdException("사용이 중지된 계정입니다.");
        }

        // 3. 비밀번호 일치 여부 확인
        boolean isMatch = passwordEncoder.matches(inputPwd, resultList.getFirst().getAcctPwd());
        if (!isMatch) {
            throw new SshdException("비밀번호가 일치하지 않습니다.");
        }

        // 로그인 성공 시 JWT 토큰 생성
        String accessToken = jwtUtil.generateAccessToken(acctId);
        String refreshToken = jwtUtil.generateRefreshToken(acctId);

        COJwtAdmAcctTokenDTO resultData = new COJwtAdmAcctTokenDTO();
        resultData.setAcctId(acctId);
        resultData.setAccTkn(accessToken);
        resultData.setRefTkn(refreshToken);
        resultData.setDevId(devId);
        resultData.setRefExpDttm(DateUtil.convertDateToLocalDateTime(jwtUtil.getExpirationDateFromToken(refreshToken, true)));
        resultData.setCOAdmAcctMngtSub01DTOList(resultList);

        cOJwtAdmAcctTokenBIZ.saveToken(resultData);

        resultData.getCOAdmAcctMngtSub01DTOList().getFirst().setAcctPwd("");
        resultData.getCOAdmAcctMngtSub01DTOList().getFirst().setFinlChgId("");
        return SshdResponse.success(resultData);
    }

    @Transactional
    public SshdResponse<Void> logout(COAdmAcctMngtDTO cOAdmAcctMngtDTO) {
        String acctId = cOAdmAcctMngtDTO.getAcctId();
        String devId = cOAdmAcctMngtDTO.getDevId();

        COJwtAdmAcctTokenDTO cOJwtAdmAcctTokenDTO = new COJwtAdmAcctTokenDTO();
        cOJwtAdmAcctTokenDTO.setAcctId(acctId);
        cOJwtAdmAcctTokenDTO.setDevId(devId);

        cOJwtAdmAcctTokenBIZ.deleteTokenByAcctAndDevice(cOJwtAdmAcctTokenDTO);

        return SshdResponse.success();
    }

    /**
     * 현재 로그인한 사용자 정보 조회
     */
    @Transactional(readOnly = true)
    public SshdResponse<COAdmAcctMngtDTO> getCurrentUser(HttpServletRequest request) {
        try {
            String token = jwtUtil.extractTokenFromRequest(request);

            // 2. JwtUtil을 사용해서 토큰에서 사용자명 추출
            String acctId;
            try {
                // 액세스 토큰 유효성 검증
                if (!jwtUtil.validateAccessToken(token)) {
                    return SshdResponse.fail("99", "유효하지 않은 토큰입니다.");
                }

                // 토큰에서 사용자명 추출
                acctId = jwtUtil.getUsernameFromAccessToken(token);

                // username null 체크 추가
                if (acctId == null || acctId.trim().isEmpty()) {
                    return SshdResponse.fail("99", "토큰에서 사용자 정보를 찾을 수 없습니다.");
                }
            } catch (Exception e) {
                log.error("토큰에서 사용자명 추출 실패: {}", e.getMessage());
                return SshdResponse.fail("99", "유효하지 않은 토큰입니다.");
            }

            // 3. 데이터베이스에서 사용자 정보 조회
            COAdmAcctMngtDTO searchDto = new COAdmAcctMngtDTO();
            searchDto.setAcctId(acctId); // 또는 적절한 필드명

            List<COAdmAcctMngtSub01DTO> resultList = cOAdmAcctMngtMapper.selectCOAdmAcmtById(searchDto);

            if (resultList.isEmpty()) {
                return SshdResponse.fail("99", "사용자 정보를 찾을 수 없습니다.");
            }

            // 4. 민감한 정보 제거 (비밀번호 등)
            resultList.getFirst().setAcctPwd(null); // 비밀번호는 응답에서 제외

            COAdmAcctMngtDTO resultData = new COAdmAcctMngtDTO();
            resultData.setCOAdmAcctMngtSub01DTOList(resultList);

            return SshdResponse.success(resultData);

        } catch (Exception e) {
            log.error("현재 사용자 정보 조회 중 오류 발생: {}", e.getMessage(), e);
            return SshdResponse.fail("99", "사용자 정보 조회에 실패했습니다.");
        }
    }

    @Transactional(readOnly = true)
    public SshdResponse<COAdmAcctMngtDTO> selectList(COAdmAcctMngtDTO cOAdmAcctMngtDTO) {
        COAdmAcctMngtDTO resultData = new COAdmAcctMngtDTO();

        List<COAdmAcctMngtSub01DTO> resultList = cOAdmAcctMngtMapper.selectList(cOAdmAcctMngtDTO);

        resultData.setCOAdmAcctMngtSub01DTOList(resultList);

        return SshdResponse.success(resultData);
    }

    @Transactional
    public SshdResponse<COAdmAcctMngtDTO> insertList(COAdmAcctMngtDTO cOAdmAcctMngtDTO) {
        SshdResponse<COAdmAcctMngtDTO> resultData;
        int cnt = 0;

        log.debug("insert 메서드 호출됨: {}", cOAdmAcctMngtDTO);

        for (COAdmAcctMngtSub01DTO tuple : cOAdmAcctMngtDTO.getCOAdmAcctMngtSub01DTOList()) {

            Map<String, Object> checkMap = MapUtil.toMap(tuple);
            if (cOAdmAcctMngtMapper.selectExists(checkMap)) {
                throw new SshdException("이미 존재하는 계정입니다.");
            }

            tuple.setAcctPwd(passwordEncoder.encode(tuple.getAcctPwd()));
            tuple.setFrRgstId (cOAdmAcctMngtDTO.getAcctId());
            tuple.setFinlChgId(cOAdmAcctMngtDTO.getAcctId());

            cnt += cOAdmAcctMngtMapper.insert(tuple);
        }

        if (cnt > 0) {
            resultData = SshdResponse.success();
        } else {
            throw new SshdException("저장에 실패하였습니다.");
        }

        return resultData;
    }

    @Transactional
    public SshdResponse<COAdmAcctMngtDTO> updateList(COAdmAcctMngtDTO cOAdmAcctMngtDTO) {
        SshdResponse<COAdmAcctMngtDTO> resultData;
        int cnt = 0;
        for (COAdmAcctMngtSub01DTO tuple : cOAdmAcctMngtDTO.getCOAdmAcctMngtSub01DTOList()) {
            Map<String, Object> checkMap = MapUtil.toMap(tuple);
            if (!cOAdmAcctMngtMapper.selectExists(checkMap)) {
                throw new SshdException("유효하지 않은 계정ID입니다.");
            }

            tuple.setAcctPwd  (passwordEncoder.encode(tuple.getAcctPwd()));
            tuple.setFinlChgId(cOAdmAcctMngtDTO.getAcctId());

            cnt += cOAdmAcctMngtMapper.update(tuple);
        }

        if (cnt > 0) {
            resultData = SshdResponse.success();
        } else {
            throw new SshdException("저장에 실패하였습니다.");
        }

        return resultData;
    }

    /**
     * 액세스 토큰 유효성 검증
     */
    @Transactional(readOnly = true)
    public SshdResponse<Map<String, Object>> validateToken(HttpServletRequest request) {

        String token = extractTokenFromRequest(request);
        String devId = jwtUtil.extractDeviceIdFromRequest(request); // 헤더에서 기기 ID 추출

        if (StringUtil.isNullOrEmpty(token)) {
            throw new SshdException("토큰이 제공되지 않았습니다.");
        }

        try {
            // 1. 토큰 형식 및 서명 검증
            if (!jwtUtil.validateAccessToken(token)) {
                throw new SshdException("토큰이 유효하지 않습니다.");
            }

            // 2. 토큰 만료 확인
            if (jwtUtil.isTokenExpired(token, false)) {
                throw new SshdException("토큰이 만료되었습니다.");
            }

            // 3. 사용자 정보 추출
            String username = jwtUtil.getUsernameFromAccessToken(token);
            Date expiration = jwtUtil.getExpirationDateFromToken(token, false);

            // 4. DB 검증
            boolean isValidInDb = isTokenValidInDatabase(username, devId);

            Map<String, Object> result = new HashMap<>();
            result.put("valid", true);
            result.put("username", username);
            result.put("expiresAt", expiration.getTime());
            result.put("validInDatabase", isValidInDb);
            result.put("remainingTime", expiration.getTime() - System.currentTimeMillis());

            return SshdResponse.success(result);

        } catch (SshdException e) {
            throw e; // SshdException은 그대로 던짐
        } catch (Exception e) {
            log.error("토큰 검증 중 오류 발생", e);
            throw new SshdException("토큰 검증 중 오류가 발생했습니다.");
        }
    }

    /**
     * 토큰 갱신 (리프레시 토큰 사용)
     */
    @Transactional
    public SshdResponse<COJwtAdmAcctTokenDTO> refreshToken(COJwtAdmAcctTokenDTO cOJwtAdmAcctTokenDTO) {

        String refreshToken = cOJwtAdmAcctTokenDTO.getRefTkn();
        String deviceId = cOJwtAdmAcctTokenDTO.getDevId();

        if (StringUtil.isNullOrEmpty(refreshToken) || StringUtil.isNullOrEmpty(deviceId)) {
            throw new SshdException("리프레시 토큰 또는 기기 ID가 누락되었습니다.");
        }

        try {
            // 1. 리프레시 토큰 검증
            if (!jwtUtil.validateRefreshToken(refreshToken)) {
                throw new SshdException("리프레시 토큰이 유효하지 않습니다.");
            }

            // 2. 사용자 정보 추출
            String username = jwtUtil.getUsernameFromRefreshToken(refreshToken);

            // 3. DB에서 리프레시 토큰 확인
            COJwtAdmAcctTokenDTO tokenDto = new COJwtAdmAcctTokenDTO();
            tokenDto.setAcctId(username);
            tokenDto.setDevId(deviceId);

            COJwtAdmAcctTokenDTO dbToken = cOJwtAdmAcctTokenBIZ.getByAcctIdAndDevId(tokenDto);
            if (dbToken == null || !refreshToken.equals(dbToken.getRefTkn())) {
                throw new SshdException("리프레시 토큰이 일치하지 않습니다.");
            }

            // 4. 새로운 토큰 생성
            String newAccessToken = jwtUtil.generateAccessToken(username);
            String newRefreshToken = jwtUtil.generateRefreshToken(username);

            // 5. DB 업데이트
            COJwtAdmAcctTokenDTO newTokenDto = new COJwtAdmAcctTokenDTO();
            newTokenDto.setAcctId(username);
            newTokenDto.setDevId(deviceId);
            newTokenDto.setAccTkn(newAccessToken);
            newTokenDto.setRefTkn(newRefreshToken);
            newTokenDto.setRefExpDttm(DateUtil.convertDateToLocalDateTime(
                    jwtUtil.getExpirationDateFromToken(newRefreshToken, true)));

            // 기존 토큰 삭제 후 새 토큰 저장
            cOJwtAdmAcctTokenBIZ.deleteTokenByAcctAndDevice(tokenDto);
            cOJwtAdmAcctTokenBIZ.saveToken(newTokenDto);

            return SshdResponse.success(newTokenDto);

        } catch (SshdException e) {
            throw e; // SshdException은 그대로 던짐
        } catch (Exception e) {
            log.error("토큰 갱신 중 오류 발생", e);
            throw new SshdException("토큰 갱신 중 오류가 발생했습니다.");
        }
    }

    /**
     * Request에서 토큰 추출
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        }
        return header.substring(7);
    }

    /**
     * DB에서 토큰 유효성 확인
     */
    private boolean isTokenValidInDatabase(String username, String deviceId) {
        try {
            COJwtAdmAcctTokenDTO dto = new COJwtAdmAcctTokenDTO();
            dto.setAcctId(username);
            dto.setDevId(deviceId);

            COJwtAdmAcctTokenDTO dbToken = cOJwtAdmAcctTokenBIZ.getByAcctIdAndDevId(dto);
            return dbToken != null && !StringUtil.isNullOrEmpty(dbToken.getRefTkn());

        } catch (Exception e) {
            log.warn("DB 토큰 검증 중 오류 발생: {}", e.getMessage());
            return false;
        }
    }
}