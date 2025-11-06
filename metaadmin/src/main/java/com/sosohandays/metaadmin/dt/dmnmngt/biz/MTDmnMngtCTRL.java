package com.sosohandays.metaadmin.dt.dmnmngt.biz;

import com.sosohandays.metaadmin.dt.dmnmngt.dto.MTDmnMngtDTO;
import com.sosohandays.metaadmin.dt.dmnmngt.dto.MTDmnMngtSub01DTO;
import com.sosohandays.common.exception.SshdException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sosohandays.common.response.SshdResponse;
import com.sosohandays.common.util.StringUtil;

@Slf4j
@Validated
@RestController
@RequestMapping("/mt/dmnmngt")
@RequiredArgsConstructor
public class MTDmnMngtCTRL {

    private final MTDmnMngtSVC mTDmnMngtSVC;

    @PostMapping("/select")
    public ResponseEntity<SshdResponse<MTDmnMngtDTO>> select(@RequestBody MTDmnMngtDTO mTDmnMngtDTO) {
        SshdResponse<MTDmnMngtDTO> resultData;

        resultData = mTDmnMngtSVC.selectList(mTDmnMngtDTO);

        return ResponseEntity.ok(resultData);
    }

    @PostMapping("/selectDetail")
    public ResponseEntity<SshdResponse<MTDmnMngtDTO>> selectDetail(@RequestBody MTDmnMngtDTO mTDmnMngtDTO) {
        SshdResponse<MTDmnMngtDTO> resultData;

        if (StringUtil.isNullOrEmpty(mTDmnMngtDTO.getDmnId())) {
            throw new SshdException("도메인ID가 없습니다.");
        }

        resultData = mTDmnMngtSVC.selectDetail(mTDmnMngtDTO);

        return ResponseEntity.ok(resultData);
    }

    @PostMapping("/insert")
    public ResponseEntity<SshdResponse<MTDmnMngtDTO>> insert(@RequestBody MTDmnMngtDTO mTDmnMngtDTO) {
        SshdResponse<MTDmnMngtDTO> resultData;

        log.debug("mTDmnMngtDTO : {}", mTDmnMngtDTO.toString());

        // 필수값 확인
        for (int i = 0; i < mTDmnMngtDTO.getMTDmnMngtSub01DTOList().size(); i++) {
            MTDmnMngtSub01DTO tuple = mTDmnMngtDTO.getMTDmnMngtSub01DTOList().get(i);
            if (StringUtil.isNullOrEmpty(tuple.getDmnNm())) {
                throw new SshdException("도메인명이 없습니다.");
            }
            if (StringUtil.isNullOrEmpty(tuple.getDmnPscNm())) {
                throw new SshdException("도메인물리명이 없습니다.");
            }
            if (StringUtil.isNullOrEmpty(tuple.getDmnLgcNm())) {
                throw new SshdException("도메인논리명이 없습니다.");
            }
            if (StringUtil.isNullOrEmpty(tuple.getDataTypeCd())) {
                throw new SshdException("데이터타입코드가 없습니다.");
            }
        }

        resultData = mTDmnMngtSVC.insertList(mTDmnMngtDTO);

        return ResponseEntity.ok(resultData);
    }

    @PostMapping("/update")
    public ResponseEntity<SshdResponse<MTDmnMngtDTO>> update(@RequestBody MTDmnMngtDTO mTDmnMngtDTO) {
        SshdResponse<MTDmnMngtDTO> resultData;

        for (MTDmnMngtSub01DTO tuple : mTDmnMngtDTO.getMTDmnMngtSub01DTOList()) {
            if (StringUtil.isNullOrEmpty(tuple.getDmnId())) {
                throw new SshdException("도메인ID가 없습니다.");
            }
        }

        resultData = mTDmnMngtSVC.updateList(mTDmnMngtDTO);

        return ResponseEntity.ok(resultData);
    }

    @PostMapping("/insertDmnCode")
    public ResponseEntity<SshdResponse<MTDmnMngtDTO>> insertDmnCode(@RequestBody MTDmnMngtDTO mTDmnMngtDTO) {
        SshdResponse<MTDmnMngtDTO> resultData;

        if (mTDmnMngtDTO.getMTDmnMngtSub02DTOList() == null || mTDmnMngtDTO.getMTDmnMngtSub02DTOList().isEmpty()) {
            throw new SshdException("등록할 도메인코드가 없습니다.");
        }

        resultData = mTDmnMngtSVC.insertDmnCodeList(mTDmnMngtDTO);

        return ResponseEntity.ok(resultData);
    }

    @PostMapping("/updateDmnCode")
    public ResponseEntity<SshdResponse<MTDmnMngtDTO>> updateDmnCode(@RequestBody MTDmnMngtDTO mTDmnMngtDTO) {
        SshdResponse<MTDmnMngtDTO> resultData;

        if (mTDmnMngtDTO.getMTDmnMngtSub02DTOList() == null || mTDmnMngtDTO.getMTDmnMngtSub02DTOList().isEmpty()) {
            throw new SshdException("수정할 도메인코드가 없습니다.");
        }

        resultData = mTDmnMngtSVC.updateDmnCodeList(mTDmnMngtDTO);

        return ResponseEntity.ok(resultData);
    }

    @PostMapping("/insertWordLst")
    public ResponseEntity<SshdResponse<MTDmnMngtDTO>> insertWordLst(@RequestBody MTDmnMngtDTO mTDmnMngtDTO) {
        SshdResponse<MTDmnMngtDTO> resultData;

        if (mTDmnMngtDTO.getMTDmnMngtSub03DTOList() == null || mTDmnMngtDTO.getMTDmnMngtSub03DTOList().isEmpty()) {
            throw new SshdException("등록할 단어내역이 없습니다.");
        }

        resultData = mTDmnMngtSVC.insertWordLstList(mTDmnMngtDTO);

        return ResponseEntity.ok(resultData);
    }
}