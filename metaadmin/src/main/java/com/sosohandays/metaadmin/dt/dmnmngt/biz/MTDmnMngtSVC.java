package com.sosohandays.metaadmin.dt.dmnmngt.biz;

import com.sosohandays.metaadmin.dt.dmnmngt.dto.MTDmnMngtDTO;
import com.sosohandays.metaadmin.dt.dmnmngt.dto.MTDmnMngtSub01DTO;
import com.sosohandays.metaadmin.dt.dmnmngt.dto.MTDmnMngtSub02DTO;
import com.sosohandays.metaadmin.dt.dmnmngt.dto.MTDmnMngtSub03DTO;
import com.sosohandays.common.exception.SshdException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sosohandays.common.response.SshdResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MTDmnMngtSVC {

    private final MTDmnMngtMapper mTDmnMngtMapper;

    /**
     * 조건에 맞는 도메인 목록을 조회합니다.
     */
    @Transactional(readOnly = true)
    public SshdResponse<MTDmnMngtDTO> selectList(MTDmnMngtDTO mTDmnMngtDTO) {
        MTDmnMngtDTO resultData = new MTDmnMngtDTO();

        log.debug("selectList mTDmnMngtDTO : {}", mTDmnMngtDTO);

        List<MTDmnMngtSub01DTO> resultList = mTDmnMngtMapper.selectByCond(mTDmnMngtDTO);
        resultData.setMTDmnMngtSub01DTOList(resultList);

        return SshdResponse.success(resultData);
    }

    /**
     * 도메인 상세 정보를 조회합니다. (도메인 기본 + 코드 + 단어내역)
     */
    @Transactional(readOnly = true)
    public SshdResponse<MTDmnMngtDTO> selectDetail(MTDmnMngtDTO mTDmnMngtDTO) {
        MTDmnMngtDTO resultData = new MTDmnMngtDTO();

        log.debug("selectDetail mTDmnMngtDTO : {}", mTDmnMngtDTO);

        // 1. 도메인 기본 정보 조회
        List<MTDmnMngtSub01DTO> dmnList = mTDmnMngtMapper.selectByCond(mTDmnMngtDTO);
        resultData.setMTDmnMngtSub01DTOList(dmnList);

        if (!dmnList.isEmpty()) {
            // 2. 도메인 코드 조회
            List<MTDmnMngtSub02DTO> dmnCodeList = mTDmnMngtMapper.selectDmnCodeList(mTDmnMngtDTO);
            resultData.setMTDmnMngtSub02DTOList(dmnCodeList);

            // 3. 도메인-단어 매핑 조회
            List<MTDmnMngtSub03DTO> wordLstList = mTDmnMngtMapper.selectWordLstList(mTDmnMngtDTO);
            resultData.setMTDmnMngtSub03DTOList(wordLstList);
        }

        return SshdResponse.success(resultData);
    }

    /**
     * 새로운 도메인을 등록합니다.
     */
    @Transactional
    public SshdResponse<MTDmnMngtDTO> insertList(MTDmnMngtDTO mTDmnMngtDTO) {
        SshdResponse<MTDmnMngtDTO> resultData;
        int cnt = 0;

        log.debug("insertList mTDmnMngtDTO : {}", mTDmnMngtDTO);

        for (MTDmnMngtSub01DTO tuple : mTDmnMngtDTO.getMTDmnMngtSub01DTOList()) {
            log.debug("tuple : {}", tuple.toString());

            // 중복 확인
            Map<String, Object> checkMap = new HashMap<>();
            checkMap.put("dmnLgcNm", tuple.getDmnLgcNm());
            if (mTDmnMngtMapper.selectExists(checkMap)) {
                throw new SshdException(tuple.getDmnLgcNm() + "는 이미 존재하는 도메인논리명입니다.");
            }

            checkMap.clear();
            checkMap.put("dmnPscNm", tuple.getDmnPscNm());
            if (mTDmnMngtMapper.selectExists(checkMap)) {
                throw new SshdException(tuple.getDmnPscNm() + "는 이미 존재하는 도메인물리명입니다.");
            }

            // 도메인ID 채번
            tuple.setDmnId(mTDmnMngtMapper.selectNextDmnId());

            tuple.setFrRgstId("admin");
            tuple.setFinlChgId("admin");

            cnt += mTDmnMngtMapper.insert(tuple);
        }

        if (cnt > 0) {
            resultData = SshdResponse.success();
        } else {
            throw new SshdException("저장에 실패하였습니다.");
        }

        return resultData;
    }

    /**
     * 기존 도메인 정보를 수정합니다.
     */
    @Transactional
    public SshdResponse<MTDmnMngtDTO> updateList(MTDmnMngtDTO mTDmnMngtDTO) {
        SshdResponse<MTDmnMngtDTO> resultData;
        int cnt = 0;

        log.debug("updateList mTDmnMngtDTO : {}", mTDmnMngtDTO);

        for (MTDmnMngtSub01DTO tuple : mTDmnMngtDTO.getMTDmnMngtSub01DTOList()) {
            Map<String, Object> checkMap = new HashMap<>();
            checkMap.put("dmnId", tuple.getDmnId());
            if (!mTDmnMngtMapper.selectExists(checkMap)) {
                throw new SshdException(tuple.getDmnId() + "는 존재하지 않는 도메인ID입니다.");
            }

            tuple.setFinlChgId("admin");

            cnt += mTDmnMngtMapper.update(tuple);
        }

        if (cnt > 0) {
            resultData = SshdResponse.success();
        } else {
            throw new SshdException("저장에 실패하였습니다.");
        }

        return resultData;
    }

    /**
     * 도메인 코드를 등록합니다.
     */
    @Transactional
    public SshdResponse<MTDmnMngtDTO> insertDmnCodeList(MTDmnMngtDTO mTDmnMngtDTO) {
        SshdResponse<MTDmnMngtDTO> resultData;
        int cnt = 0;

        for (MTDmnMngtSub02DTO tuple : mTDmnMngtDTO.getMTDmnMngtSub02DTOList()) {
            // 중복 확인
            Map<String, Object> checkMap = new HashMap<>();
            checkMap.put("dmnId", tuple.getDmnId());
            checkMap.put("cdId", tuple.getCdId());
            if (mTDmnMngtMapper.selectDmnCodeExists(checkMap)) {
                throw new SshdException("이미 존재하는 도메인코드입니다.");
            }

            tuple.setFrRgstId("admin");
            tuple.setFinlChgId("admin");

            cnt += mTDmnMngtMapper.insertDmnCode(tuple);
        }

        if (cnt > 0) {
            resultData = SshdResponse.success();
        } else {
            throw new SshdException("저장에 실패하였습니다.");
        }

        return resultData;
    }

    /**
     * 도메인 코드를 수정합니다.
     */
    @Transactional
    public SshdResponse<MTDmnMngtDTO> updateDmnCodeList(MTDmnMngtDTO mTDmnMngtDTO) {
        SshdResponse<MTDmnMngtDTO> resultData;
        int cnt = 0;

        for (MTDmnMngtSub02DTO tuple : mTDmnMngtDTO.getMTDmnMngtSub02DTOList()) {
            // 존재 확인
            Map<String, Object> checkMap = new HashMap<>();
            checkMap.put("dmnId", tuple.getDmnId());
            checkMap.put("cdId", tuple.getCdId());
            if (!mTDmnMngtMapper.selectDmnCodeExists(checkMap)) {
                throw new SshdException("존재하지 않는 도메인코드입니다.");
            }

            tuple.setFinlChgId("admin");

            cnt += mTDmnMngtMapper.updateDmnCode(tuple);
        }

        if (cnt > 0) {
            resultData = SshdResponse.success();
        } else {
            throw new SshdException("저장에 실패하였습니다.");
        }

        return resultData;
    }

    /**
     * 도메인-단어 매핑을 등록합니다.
     */
    @Transactional
    public SshdResponse<MTDmnMngtDTO> insertWordLstList(MTDmnMngtDTO mTDmnMngtDTO) {
        SshdResponse<MTDmnMngtDTO> resultData;
        int cnt = 0;

        for (MTDmnMngtSub03DTO tuple : mTDmnMngtDTO.getMTDmnMngtSub03DTOList()) {
            // 객체타입코드를 도메인(02)으로 설정
            tuple.setObjTypeCd("02");

            tuple.setFrRgstId("admin");
            tuple.setFinlChgId("admin");

            cnt += mTDmnMngtMapper.insertWordLst(tuple);
        }

        if (cnt > 0) {
            resultData = SshdResponse.success();
        } else {
            throw new SshdException("저장에 실패하였습니다.");
        }

        return resultData;
    }
}