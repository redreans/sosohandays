package com.sosohandays.metaadmin.co.handler;

import com.sosohandays.common.exception.SshdException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.sosohandays.common.response.SshdResponse;

@ControllerAdvice
@Slf4j
public class SshExceptionHandler {
    @ExceptionHandler(SshdException.class)
    public ResponseEntity<SshdResponse<Object>> handleSshdException(SshdException ex) {
        // 예외 발생 시 로그 기록
        log.error("SshdException 발생: {}", ex.getMessage(), ex); // ex를 포함하여 스택 트레이스까지 로그에 남김

        SshdResponse<Object> errorResponse = new SshdResponse<>();
        // 오류 응답에 필요한 정보 설정
        errorResponse.setResultCten(ex.getMessage()); // SshdException의 메시지 사용
        errorResponse.setResultCd  ("99"           );
        errorResponse.setData      (null           ); // 오류 발생 시 데이터는 null 또는 빈 리스트

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST); // 예: 400 Bad Request
    }
}
