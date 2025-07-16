package com.sosohandays.metaadmin.co.admacctmngt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 관리자 계정 상세 정보 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class COAdmAcctMngtSub01DTO {
    /**
     * 계정 ID (Primary Key)
     */
    private String acctId;

    /**
     * 계정 비밀번호 (암호화된 값)
     */
    private String acctPwd;

    /**
     * 계정명
     */
    private String acctNm;

    /**
     * 계정 권한 코드
     */
    private String accRoleCd;

    /**
     * 사용 여부 (Y: 사용, N: 미사용)
     */
    private String useYn;

    /**
     * 최초 등록 일시
     */
    private LocalDateTime frRgstDttm;

    /**
     * 최초 등록자 ID
     */
    private String frRgstId;

    /**
     * 최종 변경 일시
     */
    private LocalDateTime finlChgDttm;

    /**
     * 최종 변경자 ID
     */
    private String finlChgId;
}
