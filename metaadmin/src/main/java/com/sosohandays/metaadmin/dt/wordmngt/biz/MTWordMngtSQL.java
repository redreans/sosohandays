package com.sosohandays.metaadmin.dt.wordmngt.biz;

import com.sosohandays.metaadmin.dt.wordmngt.dto.MTWordMngtDTO;
import com.sosohandays.metaadmin.dt.wordmngt.dto.MTWordMngtSub01DTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MTWordMngtSQL {
    // 단어 조회
    List<MTWordMngtSub01DTO> selectByCond(MTWordMngtDTO dto);

    // 존재유무 확인
    boolean selectExists(Map map);

    // WORD_ID 채번
    String selectNextWordId();

    int insert(MTWordMngtSub01DTO dto);

    int update(MTWordMngtSub01DTO dto);
}