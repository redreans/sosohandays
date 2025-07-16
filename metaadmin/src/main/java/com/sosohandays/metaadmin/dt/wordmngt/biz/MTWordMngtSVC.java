package com.sosohandays.metaadmin.dt.wordmngt.biz;

import com.sosohandays.metaadmin.dt.wordmngt.dto.MTWordMngtDTO;
import com.sosohandays.metaadmin.dt.wordmngt.dto.MTWordMngtSub01DTO;
import com.sosohandays.common.exception.SshdException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sosohandays.common.response.SshdResponse;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MTWordMngtSVC {

    private final MTWordMngtMapper mTWordMngtMapper;

    /**
     * 조건에 맞는 용어 목록을 조회합니다.
     * @param mTWordMngtDTO 검색 조건
     * @return 용어 DTO 목록
     */
    @Transactional(readOnly = true)
    public SshdResponse<MTWordMngtDTO> selectList(MTWordMngtDTO mTWordMngtDTO) {
        MTWordMngtDTO resultData = new MTWordMngtDTO();

        log.debug("selectList mTWordMngtDTO : {}", mTWordMngtDTO);

        resultData.setMTWordMngtSub01DTOList(mTWordMngtMapper.selectByCond(mTWordMngtDTO));

        return SshdResponse.success(resultData);
    }

    /**
     * 새로운 용어를 등록합니다.
     * @param mTWordMngtDTO 등록할 용어 정보
     * @return 등록 성공 시 true
     * @throws SshdException 이미 존재하는 ID일 경우 발생
     */
    @Transactional
    public SshdResponse<MTWordMngtDTO> insertList(MTWordMngtDTO mTWordMngtDTO) {
        SshdResponse<MTWordMngtDTO> resultData;
        int cnt = 0;

        log.debug("insertList mTWordMngtDTO : {}", mTWordMngtDTO);

        for (MTWordMngtSub01DTO tuple : mTWordMngtDTO.getMTWordMngtSub01DTOList()) {
            log.debug("tuple : {}", tuple.toString());

            Map<String, Object> checkMap = new HashMap<>();
            checkMap.put("wordLgcNm", tuple.getWordLgcNm());
            if (mTWordMngtMapper.selectExists(checkMap)) {
                throw new SshdException(tuple.getWordLgcNm() + "는 이미 존재하는 단어물리명입니다.");
            }

            checkMap.clear();
            checkMap.put("wordPscNm", tuple.getWordPscNm());
            if (mTWordMngtMapper.selectExists(checkMap)) {
                throw new SshdException(tuple.getWordPscNm() + "는 이미 존재하는 단어논리명입니다.");
            }

            tuple.setWordId(mTWordMngtMapper.selectNextWordId());

            tuple.setFrRgstId ("admin");
            tuple.setFinlChgId("admin");

            cnt += mTWordMngtMapper.insert(tuple);
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
    public SshdResponse<MTWordMngtDTO> updateList(MTWordMngtDTO mTWordMngtDTO) {
        SshdResponse<MTWordMngtDTO> resultData;
        int cnt = 0;

        log.debug("updateList mTWordMngtDTO : {}", mTWordMngtDTO);

        for (MTWordMngtSub01DTO tuple : mTWordMngtDTO.getMTWordMngtSub01DTOList()) {
            Map<String, Object> checkMap = new HashMap<>();
            checkMap.put("wordId", tuple.getWordId());
            if (!mTWordMngtMapper.selectExists(checkMap)) {
                throw new SshdException(tuple.getWordId() + "는 존재하지 않는 단어ID입니다.");
            }

            tuple.setFinlChgId("admin");

            cnt += mTWordMngtMapper.update(tuple);
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