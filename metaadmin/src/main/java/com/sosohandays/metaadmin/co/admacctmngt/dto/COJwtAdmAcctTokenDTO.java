package com.sosohandays.metaadmin.co.admacctmngt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
}