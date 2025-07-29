package com.sosohandays.metaadmin.dt.wordmngt.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 단어 상세 정보 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MTWordMngtSub01DTO {
    /** 단어 ID */
    @Size(max = 30)
    private String wordId;

    /** 단어 */
    @Size(max = 100)
    private String wordNm;

    /** 단어 물리명 */
    @Size(max = 100)
    private String wordPscNm;

    /** 단어 논리명 */
    @Size(max = 100)
    private String wordLgcNm;

    /** 설명 */
    @Size(max = 1000)
    private String descCten;

    /** 사용 여부 */
    @Size(max = 1)
    private String useYn;

    /** 최초 등록 일시 */
    private LocalDateTime frRgstDttm;

    /** 최초 등록자 ID */
    private String frRgstId;

    /** 최종 수정 일시 */
    private LocalDateTime finlChgDttm;

    /** 최종 수정자 ID */
    private String finlChgId;
}