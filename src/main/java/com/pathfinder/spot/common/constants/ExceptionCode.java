package com.pathfinder.spot.common.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {

    INVALID_REQUEST(1000, "올바르지 않은 요청입니다."),
    INVALID_ACCESS_TOKEN(1001, "유효하지 않은 엑세스 토큰입니다."),
    EXPIRED_ACCESS_TOKEN(1002, "만료된 엑세스 토큰입니다."),
    EXPIRED_REFRESH_TOKEN(1004, "만료된 리프레쉬 토큰입니다."),
    INTERNAL_SERVER_ERROR(9999, "서버에서 에러가 발생하였습니다.");

    private final int code;
    private final String message;
}