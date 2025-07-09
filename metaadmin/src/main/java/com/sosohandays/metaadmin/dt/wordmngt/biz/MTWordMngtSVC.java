package com.sosohandays.metaadmin.dt.wordmngt.biz;

import com.sosohandays.metaadmin.dt.wordmngt.db.MTWordMngtDTO;
import com.sosohandays.metaadmin.dt.wordmngt.db.MTWordMngtSQL;
import exception.SshdException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import response.SshdResponse;

import java.util.List;

@Service
@RequiredArgsConstructor // final 필드에 대한 생성자를 자동으로 주입해줍니다.
public class MTWordMngtSVC {

    private final MTWordMngtSQL mTWordMngtSQL;
    private static final Logger log = LoggerFactory.getLogger(MTWordMngtSVC.class);

    /**
     * 조건에 맞는 용어 목록을 조회합니다.
     * @param mTWordMngtDTO 검색 조건
     * @return 용어 DTO 목록
     */
    public SshdResponse<List<MTWordMngtDTO>> getList(MTWordMngtDTO mTWordMngtDTO) {
        log.debug("getList mTWordMngtDTO : {}", mTWordMngtDTO);

        return SshdResponse.success(mTWordMngtSQL.selectByCond(mTWordMngtDTO));
    }

    /**
     * 새로운 용어를 등록합니다.
     * @param mTWordMngtDTO 등록할 용어 정보
     * @return 등록 성공 시 true
     * @throws SshdException 이미 존재하는 ID일 경우 발생
     */
    @Transactional
    public SshdResponse<List<MTWordMngtDTO>> insert(MTWordMngtDTO mTWordMngtDTO) {
        log.debug("insert mTWordMngtDTO : {}", mTWordMngtDTO);

        SshdResponse<List<MTWordMngtDTO>> resultData;
        if (mTWordMngtSQL.selectExistsById(mTWordMngtDTO.getWordId())) {
            resultData = SshdResponse.fail("10", "이미 존재하는 단어 ID입니다.");
        }
        else {
            int cnt = mTWordMngtSQL.insert(mTWordMngtDTO);

            if (cnt > 0) {
                resultData = SshdResponse.success();
            }
            else {
                resultData = SshdResponse.fail("40", "저장 실패");
            }
        }
        return resultData;
    }

    /**
     * 기존 용어 정보를 수정합니다.
     * @param mTWordMngtDTO 수정할 용어 정보
     * @return 수정 성공 시 true
     * @throws SshdException 존재하지 않는 ID일 경우 발생
     */
    @Transactional
    public SshdResponse<List<MTWordMngtDTO>> update(MTWordMngtDTO mTWordMngtDTO) {
        log.debug("update mTWordMngtDTO : {}", mTWordMngtDTO);

        SshdResponse<List<MTWordMngtDTO>> resultData;
        if (!mTWordMngtSQL.selectExistsById(mTWordMngtDTO.getWordId())) {
            resultData = SshdResponse.fail("10", "존재하지 않는 단어 ID입니다.");
        }
        else {
            int cnt = mTWordMngtSQL.update(mTWordMngtDTO);

            if (cnt > 0) {
                // 성공 시 데이터 없이 성공 응답 반환
                resultData = SshdResponse.success();
            }
            else {
                resultData = SshdResponse.fail("40", "수정 실패");
            }
        }
        return resultData;
    }

    /**
     * 용어를 삭제합니다.
     * @param mTWordMngtDTO 삭제할 용어 정보 (wordId 필요)
     * @return 삭제 성공 시 true
     * @throws SshdException 존재하지 않는 ID일 경우 발생
     */
    @Transactional
    public SshdResponse<List<MTWordMngtDTO>> delete(MTWordMngtDTO mTWordMngtDTO) {
        log.debug("delete mTWordMngtDTO : {}", mTWordMngtDTO);

        SshdResponse<List<MTWordMngtDTO>> resultData;
        if (!mTWordMngtSQL.selectExistsById(mTWordMngtDTO.getWordId())) {
            resultData = SshdResponse.fail("10", "존재하지 않는 단어 ID입니다.");
        }
        else {
            int cnt = mTWordMngtSQL.delete(mTWordMngtDTO);

            if (cnt > 0) {
                // 성공 시 데이터 없이 성공 응답 반환
                resultData = SshdResponse.success();
            }
            else {
                resultData = SshdResponse.fail("40", "삭제 실패");
            }
        }
        return resultData;
    }
}