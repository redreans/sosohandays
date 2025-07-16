package com.sosohandays.metaadmin.dt.wordmngt.biz;

import com.sosohandays.metaadmin.dt.wordmngt.dto.MTWordMngtDTO;
import com.sosohandays.metaadmin.dt.wordmngt.dto.MTWordMngtSub01DTO;
import exception.SshdException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import response.SshdResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor // final 필드에 대한 생성자를 자동으로 주입해줍니다.
public class MTWordMngtSVC {

    private final MTWordMngtSQL mTWordMngtSQL;

    /**
     * 조건에 맞는 용어 목록을 조회합니다.
     * @param mTWordMngtDTO 검색 조건
     * @return 용어 DTO 목록
     */
    public SshdResponse<MTWordMngtDTO> getList(MTWordMngtDTO mTWordMngtDTO) {
        log.debug("getList mTWordMngtDTO : {}", mTWordMngtDTO);
        MTWordMngtDTO resultData = new MTWordMngtDTO();

        List<MTWordMngtSub01DTO> resultList = mTWordMngtSQL.selectByCond(mTWordMngtDTO);
        resultData.setMTWordMngtSub01DTOList(resultList);

        return SshdResponse.success(resultData);
    }

    /**
     * 새로운 용어를 등록합니다.
     * @param mTWordMngtDTO 등록할 용어 정보
     * @return 등록 성공 시 true
     * @throws SshdException 이미 존재하는 ID일 경우 발생
     */
    @Transactional
    public SshdResponse<MTWordMngtDTO> insert(MTWordMngtDTO mTWordMngtDTO) {
        log.debug("insert mTWordMngtDTO : {}", mTWordMngtDTO);

        SshdResponse<MTWordMngtDTO> resultData;
        int cnt = 0;

        for (MTWordMngtSub01DTO tuple : mTWordMngtDTO.getMTWordMngtSub01DTOList()) {
            log.debug("tuple : {}", tuple.toString());

            Map<String, Object> checkMap = new HashMap<>();
            checkMap.put("wordLgcNm", tuple.getWordLgcNm());
            if (mTWordMngtSQL.selectExists(checkMap)) {
                throw new SshdException(tuple.getWordLgcNm() + "는 이미 존재하는 단어물리명입니다.");
            }

            checkMap.clear();
            checkMap.put("wordPscNm", tuple.getWordPscNm());
            if (mTWordMngtSQL.selectExists(checkMap)) {
                throw new SshdException(tuple.getWordPscNm() + "는 이미 존재하는 단어논리명입니다.");
            }

            tuple.setWordId(mTWordMngtSQL.selectNextWordId());

            tuple.setFinlChgId("admin");
            tuple.setFrRgstId ("admin");

            cnt = mTWordMngtSQL.insert(tuple);
        }

        if (cnt > 0) {
            resultData = SshdResponse.success();
        }
        else {
            throw new SshdException("저장에 실패하였습니다.");
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
    public SshdResponse<MTWordMngtDTO> update(MTWordMngtDTO mTWordMngtDTO) {
        log.debug("update mTWordMngtDTO : {}", mTWordMngtDTO);

        SshdResponse<MTWordMngtDTO> resultData;
        int cnt = 0;

        for (MTWordMngtSub01DTO tuple : mTWordMngtDTO.getMTWordMngtSub01DTOList()) {
            Map<String, Object> checkMap = new HashMap<>();
            checkMap.put("wordId", tuple.getWordId());
            if (!mTWordMngtSQL.selectExists(checkMap)) {
                throw new SshdException(tuple.getWordId() + "는 존재하지 않는 단어ID입니다.");
            }

            tuple.setFrRgstId ("admin");

            cnt = mTWordMngtSQL.update(tuple);
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