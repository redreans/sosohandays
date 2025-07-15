package com.sosohandays.metaadmin.dt.wordmngt.biz;

import com.sosohandays.metaadmin.dt.wordmngt.dto.MTWordMngtDTO;
import com.sosohandays.metaadmin.dt.wordmngt.dto.MTWordMngtSub01DTO;
import exception.SshdException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import response.SshdResponse;
import util.StringUtil;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/mt/wordmngt")
@RequiredArgsConstructor
public class MTWordMngtCTRL {

    private final MTWordMngtSVC mTWordMngtSVC;

    @PostMapping("/select")
    public ResponseEntity<SshdResponse<MTWordMngtDTO>> select(@RequestBody MTWordMngtDTO mTWordMngtDTO) {
        SshdResponse<MTWordMngtDTO> resultData;

        resultData = mTWordMngtSVC.getList(mTWordMngtDTO);

        return ResponseEntity.ok(resultData);
    }

    @PostMapping("/insert")
    public ResponseEntity<SshdResponse<MTWordMngtDTO>> insert(@RequestBody MTWordMngtDTO mTWordMngtDTO) {
        SshdResponse<MTWordMngtDTO> resultData;

        log.debug("mTWordMngtDTO : {}", mTWordMngtDTO.toString());

        // 필수값 확인
        for (int i = 0; i < mTWordMngtDTO.getMTWordMngtSub01DTOList().size(); i++) {
            MTWordMngtSub01DTO tuple = mTWordMngtDTO.getMTWordMngtSub01DTOList().get(i);
            if (StringUtil.isNullOrEmpty(tuple.getWordPscNm())) {
                throw new SshdException("단어물리명이 없습니다.");
            }
            if (StringUtil.isNullOrEmpty(tuple.getWordLgcNm())) {
                throw new SshdException("단어논리명이 없습니다.");
            }
            if (StringUtil.isNullOrEmpty(tuple.getDescCten())) {
                throw new SshdException("단어설명이 없습니다.");
            }
        }

        resultData = mTWordMngtSVC.insert(mTWordMngtDTO);

        return ResponseEntity.ok(resultData);
    }

    @PostMapping("/update")
    public ResponseEntity<SshdResponse<MTWordMngtDTO>> update(@RequestBody MTWordMngtDTO mTWordMngtDTO) {
        SshdResponse<MTWordMngtDTO> resultData;

        for (MTWordMngtSub01DTO tuple : mTWordMngtDTO.getMTWordMngtSub01DTOList()) {
            if (StringUtil.isNullOrEmpty(tuple.getWordId())) {
                throw new SshdException("단어ID가 없습니다.");
            }
        }

        resultData = mTWordMngtSVC.update(mTWordMngtDTO);

        return ResponseEntity.ok(resultData);
    }
}