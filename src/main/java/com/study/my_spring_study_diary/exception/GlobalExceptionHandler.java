package com.study.my_spring_study_diary.exception;
/*
전역 예외처리기
@RestcontrollerAdvice : 모든 컨트롤러에서 발생하는 예외를 처리
@ControllerAdvice + @ResponseBody의 조합
 */

import com.study.my_spring_study_diary.global.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
/*
IllegalArgumentExcetion처리
유효성 검증 실패, 잘못 된 요청 등
 */
@ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(
            IllegalArgumentException e){
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error("INVALID_INPUT",e.getMessage()));
}
/*
그 외 모든 예외처리
 */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponse<Void>>handleException(Exception e){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("INTERNAL_SERVER_ERROR","서버 내부 오류가 발생했습니다."));
    }
}
