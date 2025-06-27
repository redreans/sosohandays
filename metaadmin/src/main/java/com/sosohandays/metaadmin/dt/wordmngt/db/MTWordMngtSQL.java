package com.sosohandays.metaadmin.dt.wordmngt.db;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MTWordMngtSQL {
    // 단어 조회
    List<MTWordMngtDTO> selectByCond(MTWordMngtDTO dto);

    // 존재유무 확인
    boolean selectExistsById(String wordId);

    int insert(MTWordMngtDTO dto);

    int update(MTWordMngtDTO dto);

    int delete(MTWordMngtDTO dto);
}