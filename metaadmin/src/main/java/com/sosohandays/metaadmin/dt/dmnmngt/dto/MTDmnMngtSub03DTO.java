package com.sosohandays.metaadmin.dt.dmnmngt.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 도메인-단어 매핑 DTO (TB_CO0005L_WORDLST에서 도메인 관련)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MTDmnMngtSub03DTO {
    /** 객체 ID (도메인 ID) */
    @Size(max = 30)
    private String objId;

    /** 객체 타입 코드 (02: 도메인) */
    @Size(max = 2)
    private String objTypeCd;

    /** 단어 ID */
    @Size(max = 30)
    private String wordId;

    /** 정렬 번호 */
    private Integer srtNo;

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

    // 연관 정보 (조회용)
    /** 단어명 */
    private String wordNm;

    /** 단어 물리명 */
    private String wordPscNm;

    /** 단어 논리명 */
    private String wordLgcNm;
}