package com.sosohandays.metaadmin.dt.wordmngt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MTWordMngtDTO {

    private String wordId;

    private String wordPscNm;

    private String wordLgcNm;

    private String useYn;

    @JsonProperty("mTWordMngtSub01DTOList")
    private List<MTWordMngtSub01DTO> mTWordMngtSub01DTOList;
}