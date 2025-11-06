package com.sosohandays.metaadmin.dt.dmnmngt.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 도메인 코드 DTO (TB_CO0003L_DMNCODE)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MTDmnMngtSub02DTO {
    /** 도메인 ID */
    @Size(max = 30)
    private String dmnId;

    /** 코드 ID */
    @Size(max = 10)
    private String cdId;

    /** 코드명 */
    @Size(max = 100)
    private String cdNm;

    /** 코드 설명 */
    @Size(max = 500)
    private String cdDesc;

    /** 데이터 타입 코드 */
    @Size(max = 2)
    private String dataTypeCd;

    /** 정렬 번호 */
    private Integer srtNo;

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