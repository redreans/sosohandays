package com.sosohandays.metaadmin.co.admacctmngt.biz;

import com.sosohandays.common.response.SshdResponse;
import com.sosohandays.metaadmin.co.admacctmngt.dto.COAdmAcctMngtDTO;
import com.sosohandays.metaadmin.co.admacctmngt.dto.COJwtAdmAcctTokenDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
