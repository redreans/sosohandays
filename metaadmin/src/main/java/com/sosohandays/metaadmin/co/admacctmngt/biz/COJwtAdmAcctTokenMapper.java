package com.sosohandays.metaadmin.co.admacctmngt.biz;

import com.sosohandays.metaadmin.co.admacctmngt.dto.COJwtAdmAcctTokenDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface COJwtAdmAcctTokenMapper {

    int insertToken(COJwtAdmAcctTokenDTO tokenDTO);

    // 특정 계정+기기 리프레시 토큰 조회
    COJwtAdmAcctTokenDTO selectByAcctIdAndDevId(COJwtAdmAcctTokenDTO tokenDTO);

    // 특정 계정 모든 기기 리프레시 토큰 조회
    List<COJwtAdmAcctTokenDTO> selectAllByAcctId(COJwtAdmAcctTokenDTO tokenDTO);

    // 특정 계정+기기 리프레시 토큰 삭제
    int deleteByAcctIdAndDevId(COJwtAdmAcctTokenDTO tokenDTO);

    // 특정 계정 모든 기기 토큰 삭제
    int deleteAllByAcctId(COJwtAdmAcctTokenDTO tokenDTO);
}