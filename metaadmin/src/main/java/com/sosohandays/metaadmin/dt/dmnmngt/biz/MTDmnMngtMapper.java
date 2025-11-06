package com.sosohandays.metaadmin.dt.dmnmngt.biz;

import com.sosohandays.metaadmin.dt.dmnmngt.dto.MTDmnMngtDTO;
import com.sosohandays.metaadmin.dt.dmnmngt.dto.MTDmnMngtSub01DTO;
import com.sosohandays.metaadmin.dt.dmnmngt.dto.MTDmnMngtSub02DTO;
import com.sosohandays.metaadmin.dt.dmnmngt.dto.MTDmnMngtSub03DTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MTDmnMngtMapper {

    // 도메인 기본 정보 관련
    List<MTDmnMngtSub01DTO> selectByCond(MTDmnMngtDTO dto);
    boolean selectExists(Map<String, Object> map);
    String selectNextDmnId();
    int insert(MTDmnMngtSub01DTO dto);
    int update(MTDmnMngtSub01DTO dto);

    // 도메인 코드 관련
    List<MTDmnMngtSub02DTO> selectDmnCodeList(MTDmnMngtDTO dto);
    boolean selectDmnCodeExists(Map<String, Object> map);
    int insertDmnCode(MTDmnMngtSub02DTO dto);
    int updateDmnCode(MTDmnMngtSub02DTO dto);
    int deleteDmnCode(MTDmnMngtSub02DTO dto);

    // 도메인-단어 매핑 관련
    List<MTDmnMngtSub03DTO> selectWordLstList(MTDmnMngtDTO dto);
    int insertWordLst(MTDmnMngtSub03DTO dto);
    int deleteWordLst(MTDmnMngtSub03DTO dto);
}