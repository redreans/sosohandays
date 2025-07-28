package com.sosohandays.metaadmin.co.admacctmngt.biz;

import com.sosohandays.common.response.SshdResponse;
import com.sosohandays.metaadmin.co.admacctmngt.dto.COAdmAcctMngtDTO;
import com.sosohandays.metaadmin.co.admacctmngt.dto.COJwtAdmAcctTokenDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@Validated
@RestController
@RequestMapping("/co/admacctmngt")
@RequiredArgsConstructor
public class COAdmAcctMngtCTRL {
    private final COAdmAcctMngtSVC cOAdmAcctMngtSVC;

    @PostMapping("/login")
    public ResponseEntity<SshdResponse<COJwtAdmAcctTokenDTO>> login(@RequestBody COAdmAcctMngtDTO cOAdmAcctMngtDTO) {
        SshdResponse<COJwtAdmAcctTokenDTO> resultData;

        resultData = cOAdmAcctMngtSVC.login(cOAdmAcctMngtDTO);

        return ResponseEntity.ok(resultData);
    }

    @PostMapping("/logout")
    public ResponseEntity<SshdResponse<Void>> logout(@RequestBody COAdmAcctMngtDTO cOAdmAcctMngtDTO) {
        SshdResponse<Void> resultData;

        resultData = cOAdmAcctMngtSVC.logout(cOAdmAcctMngtDTO);

        return ResponseEntity.ok(resultData);
    }

    /**
     * 현재 로그인한 사용자 정보 조회
     */
    @PostMapping("/me")
    public ResponseEntity<SshdResponse<COAdmAcctMngtDTO>> getCurrentUser(HttpServletRequest request) {
        SshdResponse<COAdmAcctMngtDTO> resultData;

        resultData = cOAdmAcctMngtSVC.getCurrentUser(request);

        return ResponseEntity.ok(resultData);
    }

    @PostMapping("/select")
    public ResponseEntity<SshdResponse<COAdmAcctMngtDTO>> select(@RequestBody COAdmAcctMngtDTO cOAdmAcctMngtDTO) {
        SshdResponse<COAdmAcctMngtDTO> resultData;

        resultData = cOAdmAcctMngtSVC.selectList(cOAdmAcctMngtDTO);

        return ResponseEntity.ok(resultData);
    }

    @PostMapping("/insert")
    public ResponseEntity<SshdResponse<COAdmAcctMngtDTO>> insert(@RequestBody COAdmAcctMngtDTO cOAdmAcctMngtDTO) {
        SshdResponse<COAdmAcctMngtDTO> resultData;

        resultData = cOAdmAcctMngtSVC.insertList(cOAdmAcctMngtDTO);

        return ResponseEntity.ok(resultData);
    }

    @PostMapping("/update")
    public ResponseEntity<SshdResponse<COAdmAcctMngtDTO>> update(@RequestBody COAdmAcctMngtDTO cOAdmAcctMngtDTO) {
        SshdResponse<COAdmAcctMngtDTO> resultData;

        resultData = cOAdmAcctMngtSVC.updateList(cOAdmAcctMngtDTO);

        return ResponseEntity.ok(resultData);
    }

    /**
     * 액세스 토큰 유효성 검증
     */
    @PostMapping("/validateToken")
    public ResponseEntity<SshdResponse<Map<String, Object>>> validateToken(HttpServletRequest request) {
        SshdResponse<Map<String, Object>> resultData;

        resultData = cOAdmAcctMngtSVC.validateToken(request);

        return ResponseEntity.ok(resultData);
    }

    /**
     * 토큰 갱신 (리프레시 토큰 사용)
     */
    @PostMapping("/refreshToken")
    public ResponseEntity<SshdResponse<COJwtAdmAcctTokenDTO>> refreshToken(@RequestBody COJwtAdmAcctTokenDTO cOJwtAdmAcctTokenDTO) {
        SshdResponse<COJwtAdmAcctTokenDTO> resultData;

        resultData = cOAdmAcctMngtSVC.refreshToken(cOJwtAdmAcctTokenDTO);

        return ResponseEntity.ok(resultData);
    }
}