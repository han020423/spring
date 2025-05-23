package com.example.study1.global.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // 회원
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 틀립니다."),
    BLANK_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호는 공백일 수 없습니다"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 잘못됨"),

    // 게시물
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 게시물입니다."),
    NO_BOARD_PERMISSION(HttpStatus.FORBIDDEN, "게시물에 대한 권한이 없습니다."),

    // 댓글
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 댓글입니다."),
    NO_COMMENT_PERMISSION(HttpStatus.FORBIDDEN, "댓글에 대한 권한이 없습니다."),

    //일기
    DIARY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 일기입니다."),
    FORBIDDEN_USER(HttpStatus.FORBIDDEN, "금지된 접근입니다."),
    // 공통
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "잘못된 입력입니다."),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 이메일입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 RefreshToken입니다.");
    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
