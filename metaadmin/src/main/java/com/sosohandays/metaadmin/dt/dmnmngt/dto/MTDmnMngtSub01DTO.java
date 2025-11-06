package com.sosohandays.metaadmin.dt.dmnmngt.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 도메인 기본 정보 DTO (TB_CO0002M_DMN)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MTDmnMngtSub01DTO {
    /** 도메인 ID */
    @Size(max = 30)
    private String dmnId;

    /** 도메인명 */
    @Size(max = 100)
    private String dmnNm;

    /** 도메인 물리명 */
    @Size(max = 100)
    private String dmnPscNm;

    /** 도메인 논리명 */
    @Size(max = 100)
    private String dmnLgcNm;

    /** 데이터 타입 코드 */
    @Size(max = 2)
    private String dataTypeCd;

    /** 데이터 길이 */
    private Integer dataLen;

    /** 소수 자리수 */
    private Integer decPl;

    /** 설명 내용 */
    @Size(max = 1000)
    private String descCten;

    /** 사용 여부 */
    @Size(max = 1)
    private String useYn;

    /** 최초 등록 일시 */
    private LocalDateTime frRgstDttm;

    /** 최초 등록자 ID */
    @Size(max = 30)
    private String frRgstId;

    /** 최종 변경 일시 */
    private LocalDateTime finlChgDttm;

    /** 최종 변경자 ID */
    @Size(max = 30)
    private String finlChgId;
}