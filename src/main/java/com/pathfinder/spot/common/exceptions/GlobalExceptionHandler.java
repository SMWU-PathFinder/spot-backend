package com.pathfinder.spot.common.exceptions;

import com.pathfinder.spot.common.dto.ApiResponse;
import com.pathfinder.spot.common.exceptions.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.pathfinder.spot.common.constants.ExceptionCode.INVALID_REQUEST;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        log.warn(e.getMessage(), e);

        // 첫 번째 오류만 가져오기
        FieldError fieldError = e.getBindingResult().getFieldError();
        if (fieldError != null) {
            String errorMessage = fieldError.getDefaultMessage();

            // 에러 응답 반환
            return ResponseEntity.badRequest().body(ApiResponse.failure(String.valueOf(INVALID_REQUEST.getCode()), errorMessage));
        }
        return ResponseEntity.badRequest().body(ApiResponse.failure(String.valueOf(INVALID_REQUEST.getCode()), "잘못된 요청입니다."));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<?> handleServerError(Exception e) {
        log.error("Server Error: ", e);
        return ApiResponse.failure("SERVER_ERROR", "서버 오류가 발생했습니다.");
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleBadRequest(BadRequestException e) {
        log.error("BadRequestException: {} - {}", e.getCode(), e.getMessage());
        return ApiResponse.failure(String.valueOf(e.getCode()), e.getMessage());
    }
}