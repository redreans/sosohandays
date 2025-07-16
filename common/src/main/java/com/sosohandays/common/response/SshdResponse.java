package com.sosohandays.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SshdResponse<T> implements Serializable {

    private String resultCd;      // 결과 코드 ("00" = 성공)
    private String resultCten;    // 결과 메시지 ("" = 성공일 때 생략)
    private T data;               // 응답 데이터

    public static <T> SshdResponse<T> success(T data) {
        return SshdResponse.<T>builder()
                .resultCd("00")
                .resultCten("")
                .data(data)
                .build();
    }

    public static <T> SshdResponse<T> success() {
        return SshdResponse.<T>builder()
                .resultCd("00")
                .resultCten("")
                .build();
    }

    public static <T> SshdResponse<T> fail(String code, String message) {
        return SshdResponse.<T>builder()
                .resultCd(code)
                .resultCten(message)
                .build();
    }
}