package com.sosohandays.metaadmin.dt.wordmngt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * 단어 관리 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MTWordMngtDTO {
    /**
     * 단어 ID (고유 식별자)
     */
    private String wordId;

    /**
     * 단어 물리명 (Physical Name)
     */
    private String wordPscNm;

    /**
     * 단어 논리명 (Logical Name)
     */
    private String wordLgcNm;

    /**
     * 사용 여부 (Y/N)
     */
    private String useYn;

    /**
     * 단어 상세 정보 리스트
     */
    @JsonProperty("mTWordMngtSub01DTOList")
    private List<MTWordMngtSub01DTO> mTWordMngtSub01DTOList;
}