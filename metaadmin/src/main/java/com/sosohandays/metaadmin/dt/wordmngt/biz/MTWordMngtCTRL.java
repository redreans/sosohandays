package com.sosohandays.metaadmin.dt.wordmngt.biz;

import com.sosohandays.metaadmin.dt.wordmngt.db.MTWordMngtDTO;
import exception.SshdException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import response.SshdResponse;
import util.StringUtil;

import java.util.List;

@Validated
@RestController
@RequestMapping("/mt/wordmngt")
@RequiredArgsConstructor
public class MTWordMngtCTRL {

    private final MTWordMngtSVC mTWordMngtSVC;

    @PostMapping("/select")
    public ResponseEntity<SshdResponse<List<MTWordMngtDTO>>> select(@RequestBody MTWordMngtDTO mTWordMngtDTO) {
        SshdResponse<List<MTWordMngtDTO>> resultData = mTWordMngtSVC.getList(mTWordMngtDTO);
        return ResponseEntity.ok(resultData);
    }

    @PostMapping("/insert")
    public ResponseEntity<SshdResponse<List<MTWordMngtDTO>>> insert(@RequestBody MTWordMngtDTO mTWordMngtDTO) {
        SshdResponse<List<MTWordMngtDTO>> resultData = new SshdResponse<List<MTWordMngtDTO>>();
        String checkVailDateCten = "";
        try {
            // 필수값 확인
            if (!StringUtil.isNullOrEmpty(mTWordMngtDTO.getWordPscNm())) {
                checkVailDateCten = checkVailDateCten.isEmpty() ? "" : "\n";
                checkVailDateCten += "단어물리명이 없습니다.";
            }
            else if (!StringUtil.isNullOrEmpty(mTWordMngtDTO.getWordLgcNm())) {
                checkVailDateCten = "단어논리명이 없습니다.";
            }
            else if (!StringUtil.isNullOrEmpty(mTWordMngtDTO.getDescCten())) {
                checkVailDateCten = "단어설명이 없습니다.";
            }

            resultData = mTWordMngtSVC.insert(mTWordMngtDTO);

        }
        catch (SshdException e) {
            resultData.setResultCd("99");
            resultData.setResultCten(e.getMessage());
        }
        return ResponseEntity.ok(resultData);
    }

    @PostMapping("/update")
    public ResponseEntity<SshdResponse<List<MTWordMngtDTO>>> update(@RequestBody MTWordMngtDTO mTWordMngtDTO) {
        SshdResponse<List<MTWordMngtDTO>> resultData = mTWordMngtSVC.update(mTWordMngtDTO);
        return ResponseEntity.ok(resultData);
    }

    @PostMapping("/delete")
    public ResponseEntity<SshdResponse<List<MTWordMngtDTO>>> delete(@RequestBody MTWordMngtDTO mTWordMngtDTO) {
        SshdResponse<List<MTWordMngtDTO>> resultData = mTWordMngtSVC.delete(mTWordMngtDTO);
        return ResponseEntity.ok(resultData);
    }
}