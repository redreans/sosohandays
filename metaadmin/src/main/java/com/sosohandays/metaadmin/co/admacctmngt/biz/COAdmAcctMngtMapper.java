package com.sosohandays.metaadmin.co.admacctmngt.biz;

import com.sosohandays.metaadmin.co.admacctmngt.dto.COAdmAcctMngtDTO;
import com.sosohandays.metaadmin.co.admacctmngt.dto.COAdmAcctMngtSub01DTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface COAdmAcctMngtMapper {
    // 대상조회
    List<COAdmAcctMngtSub01DTO> selectCOAdmAcmtById(COAdmAcctMngtDTO cOAdmAcctMngtDTO);

    // 목록조회
    List<COAdmAcctMngtSub01DTO> selectList(COAdmAcctMngtDTO cOAdmAcctMngtDTO);

    // 존재유무 조회
    boolean selectExists(Map<String, Object> map);

    // 등록
    int insert(COAdmAcctMngtSub01DTO cOAdmAcctMngtSub01DTO);

    // 수정
    int update(COAdmAcctMngtSub01DTO cOAdmAcctMngtSub01DTO);
}
