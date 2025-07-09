package com.sosohandays.metaadmin.dt.wordmngt.db;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Data: Getter, Setter, RequiredArgsConstructor, ToString, EqualsAndHashCode를 모두 포함합니다.
 * @NoArgsConstructor: 파라미터가 없는 기본 생성자를 생성합니다.
 * @AllArgsConstructor: 모든 필드를 파라미터로 받는 생성자를 생성합니다.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MTWordMngtDTO {

    @Size(max = 30)
    private String wordId;

    @Size(max = 100)
    private String wordPscNm;

    @Size(max = 100)
    private String wordLgcNm;

    @Size(max = 1000)
    private String descCten;

    @Size(max = 1)
    private String useYn;

    private LocalDateTime frRgstDttm;

    private String frRgstId;

    private LocalDateTime finlChgDttm;

    private String finlChgId;
}