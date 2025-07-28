package com.sosohandays.metaadmin.co.admacctmngt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class COJwtAdmAcctTokenDTO {
    private String acctId;
    private String devId;
    private int seq;
    private String accTkn;
    private String refTkn;
    private LocalDateTime refExpDttm;
    private LocalDateTime regDttm;
    @JsonProperty("cOAdmAcctMngtSub01DTOList") // JSON 요청 시 사용할 이름 변경
    private List<COAdmAcctMngtSub01DTO> cOAdmAcctMngtSub01DTOList;
}