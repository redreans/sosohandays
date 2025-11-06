package com.sosohandays.metaadmin.dt.dmnmngt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * 도메인 관리 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MTDmnMngtDTO {
    /**
     * 도메인 ID (고유 식별자)
     */
    private String dmnId;

    /**
     * 도메인명 (Domain Name)
     */
    private String dmnNm;

    /**
     * 도메인 물리명 (Physical Name)
     */
    private String dmnPscNm;

    /**
     * 도메인 논리명 (Logical Name)
     */
    private String dmnLgcNm;

    /**
     * 데이터 타입 코드
     */
    private String dataTypeCd;

    /**
     * 데이터 길이
     */
    private Integer dataLen;

    /**
     * 소수 자리수
     */
    private Integer decPl;

    /**
     * 사용 여부 (Y/N)
     */
    private String useYn;

    /**
     * 도메인 상세 정보 리스트
     */
    @JsonProperty("mTDmnMngtSub01DTOList")
    private List<MTDmnMngtSub01DTO> mTDmnMngtSub01DTOList;

    /**
     * 도메인 코드 리스트
     */
    @JsonProperty("mTDmnMngtSub02DTOList")
    private List<MTDmnMngtSub02DTO> mTDmnMngtSub02DTOList;

    /**
     * 도메인-단어 매핑 리스트
     */
    @JsonProperty("mTDmnMngtSub03DTOList")
    private List<MTDmnMngtSub03DTO> mTDmnMngtSub03DTOList;
}