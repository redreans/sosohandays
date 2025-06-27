package com.sosohandays.metaadmin.dt.wordmngt.biz;

import com.sosohandays.metaadmin.dt.wordmngt.db.MTWordMngtDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import response.SshdResponse;

import java.util.List;

@Validated
@RestController
@RequestMapping("/metaadmin/mt/wordmngt")
@RequiredArgsConstructor
public class MTWordMngtCTRL {

    private final MTWordMngtSVC mTWordMngtSVC;

    @PostMapping("/select")
    public ResponseEntity<SshdResponse<List<MTWordMngtDTO>>> select(@RequestBody @Valid MTWordMngtDTO dto) {
        SshdResponse<List<MTWordMngtDTO>> resultData = mTWordMngtSVC.getList(dto);
        // SshdResponse는 프로젝트의 공통 응답 클래스로 가정합니다.
        return ResponseEntity.ok(resultData);
    }

    @PostMapping("/insert")
    public ResponseEntity<SshdResponse<String>> insert(@RequestBody @Valid MTWordMngtDTO dto) {
        mTWordMngtSVC.insert(dto);
        return ResponseEntity.ok(SshdResponse.success("저장 성공"));
    }

    @PostMapping("/update")
    public ResponseEntity<SshdResponse<String>> update(@RequestBody @Valid MTWordMngtDTO dto) {
        mTWordMngtSVC.update(dto);
        return ResponseEntity.ok(SshdResponse.success("수정 성공"));
    }

    @PostMapping("/delete")
    public ResponseEntity<SshdResponse<String>> delete(@RequestBody @Valid MTWordMngtDTO dto) {
        mTWordMngtSVC.delete(dto);
        return ResponseEntity.ok(SshdResponse.success("삭제 성공"));
    }
}