package com.sosohandays.metaadmin.co.admacctmngt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 관리자 계정 관리 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class COAdmAcctMngtDTO {
    /**
     * 계정 ID
     */
    private String acctId;

    /**
     * 계정명
     */
    private String acctNm;

    /**
     * 계정 비밀번호
     */
    private String acctPwd;

    /**
     * 계정권한코드
     */
    private String accRoleCd;

    /**
     * 사용 여부
     */
    private String useYn;
    /**
     * 디바이스 ID
     */
    private String devId;

    /**
     * 계정 상세 정보 리스트
     */
    @JsonProperty("cOAdmAcctMngtSub01DTOList") // JSON 요청 시 사용할 이름 변경
    private List<COAdmAcctMngtSub01DTO> cOAdmAcctMngtSub01DTOList;
}