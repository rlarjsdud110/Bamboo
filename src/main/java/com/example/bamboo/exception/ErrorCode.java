package com.example.bamboo.exception;

public enum ErrorCode {
    RETRY(400),
    SIGNUP_REQUIRED(452),
    DUPLICATE_EMAIL(453),
    NOT_FISA_STUDENT(454),
    DUPLICATE_NICKNAME(455),
    NOT_USER_INFO(456),
    POST_NOT_FOUND(462),
    COMMENT_NOT_FOUND(472),
    TOKEN_NOT_FOUND(482),
    NOT_USER_EMAIL_PASSWORD(492);

    private final int code;

    ErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}