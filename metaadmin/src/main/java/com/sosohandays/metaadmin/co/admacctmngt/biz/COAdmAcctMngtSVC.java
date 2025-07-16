package com.sosohandays.metaadmin.co.admacctmngt.biz;

import com.sosohandays.common.util.DateUtil;
import com.sosohandays.metaadmin.co.admacctmngt.dto.COAdmAcctMngtDTO;
import com.sosohandays.metaadmin.co.admacctmngt.dto.COAdmAcctMngtSub01DTO;
import com.sosohandays.common.exception.SshdException;
import com.sosohandays.metaadmin.co.admacctmngt.dto.COJwtAdmAcctTokenDTO;
import com.sosohandays.metaadmin.co.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sosohandays.common.response.SshdResponse;
import com.sosohandays.common.util.MapUtil;
import com.sosohandays.common.util.StringUtil;

import java.util.List;
import java.util.Map;

/**
 * 관리자 계정 관리 서비스 클래스
 */
@Service
@Slf4j
@RequiredArgsConstructor // final 필드를 인자로 받는 생성자를 자동으로 생성
public class COAdmAcctMngtSVC { // 클래스명 수정 완료

    private final COAdmAcctMngtMapper cOAdmAcctMngtMapper;
    private final PasswordEncoder passwordEncoder;
    private final COJwtAdmAcctTokenBIZ cOJwtAdmAcctTokenBIZ;
    private final JwtUtil jwtUtil;

    @Transactional(readOnly = true)
    public SshdResponse<COJwtAdmAcctTokenDTO> login(COAdmAcctMngtDTO cOAdmAcctMngtDTO) {

        String acctId   = cOAdmAcctMngtDTO.getAcctId();
        String inputPwd = cOAdmAcctMngtDTO.getAcctPwd();
        String devId    =  cOAdmAcctMngtDTO.getDevId();

        if (StringUtil.isNullOrEmpty(acctId) || StringUtil.isNullOrEmpty(inputPwd)) {
            throw new SshdException("아이디 또는 비밀번호가 누락되었습니다.");
        }

        // 1. 사용자 조회
        List<COAdmAcctMngtSub01DTO> resultList = cOAdmAcctMngtMapper.selectCOAdmAcmtById(cOAdmAcctMngtDTO);

        if (resultList == null) {
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
        resultData.setAcctId    (acctId      );
        resultData.setAccTkn    (accessToken );
        resultData.setRefTkn    (refreshToken);
        resultData.setDevId     (devId       );
        resultData.setRefExpDttm(DateUtil.convertDateToLocalDateTime(jwtUtil.getExpirationDateFromToken(refreshToken, true)));

        cOJwtAdmAcctTokenBIZ.saveToken(resultData);

        return SshdResponse.success(resultData);
    }

    @Transactional
    public SshdResponse<Void> logout(COAdmAcctMngtDTO cOAdmAcctMngtDTO) {
        String acctId   = cOAdmAcctMngtDTO.getAcctId();
        String devId    =  cOAdmAcctMngtDTO.getDevId();

        COJwtAdmAcctTokenDTO cOJwtAdmAcctTokenDTO = new COJwtAdmAcctTokenDTO();
        cOJwtAdmAcctTokenDTO.setAcctId(acctId);
        cOJwtAdmAcctTokenDTO.setDevId(devId);

        cOJwtAdmAcctTokenBIZ.deleteTokenByAcctAndDevice(cOJwtAdmAcctTokenDTO);

        return SshdResponse.success();
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
        for (COAdmAcctMngtSub01DTO tuple : cOAdmAcctMngtDTO.getCOAdmAcctMngtSub01DTOList()) {

            Map<String, Object> checkMap = MapUtil.toMap(tuple);
            if (cOAdmAcctMngtMapper.selectExists(checkMap)) {
                throw new SshdException("이미 존재하는 계정입니다.");
            }

            tuple.setAcctPwd(passwordEncoder.encode(tuple.getAcctPwd()));
            tuple.setFrRgstId("admin");
            tuple.setFinlChgId("admin");

            cnt += cOAdmAcctMngtMapper.insert(tuple);
        }

        if (cnt > 0) {
            resultData = SshdResponse.success();
        }
        else {
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

            tuple.setAcctPwd(passwordEncoder.encode(tuple.getAcctPwd()));
            tuple.setFinlChgId("admin");

            cnt += cOAdmAcctMngtMapper.update(tuple);
        }

        if (cnt > 0) {
            resultData = SshdResponse.success();
        }
        else {
            throw new SshdException("저장에 실패하였습니다.");
        }

        return resultData;

    }
}