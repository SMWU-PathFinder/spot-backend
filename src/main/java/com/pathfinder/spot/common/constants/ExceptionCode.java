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
    VALIDATION_ERROR(1005, "유효하지 않은 값을 입력했습니다."),
    FAIL_TO_SOCIAL_LOGIN(2000, "지원하지 않는 소셜 로그인입니다."),
    INVALID_USER(2001, "유효하지 않은 사용자입니다."),
    INVALID_MEMBER(2002, "접근 권한이 없습니다."),
    INVALID_FAV(2003, "존재하지 않는 즐겨찾기입니다."),
    INVALID_CATEGORY(2004, "존재하지 않는 카테고리입니다."),
    INVALID_REPORT(2005, "존재하지 않는 신고글입니다."),
    INTERNAL_SERVER_ERROR(9999, "서버에서 에러가 발생하였습니다.");
    private final int code;
    private final String message;
}