package com.eodi.bium.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionMessage {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다"),

    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "이미 존재하는 ID입니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "사용자를 찾을 수 없습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    TOKEN_GENERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "토큰 생성 중 서버 오류가 발생했습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    NOT_ENOUGH_ACCESS(HttpStatus.FORBIDDEN, "접근 권한이 부족합니다."),

    INSUFFICIENT_POINTS(HttpStatus.BAD_REQUEST, "포인트가 부족합니다."),
    EVENT_NOT_FOUND(HttpStatus.NOT_FOUND, "이벤트를 찾을 수 없습니다."),
    EVENT_ALREADY_ONGOING(HttpStatus.BAD_REQUEST, "이미 진행 중인 이벤트가 있습니다."),
    DRAW_ALREADY_COMPLETED(HttpStatus.BAD_REQUEST, "추첨이 이미 완료되었습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
