package com.example.bamboo.exception;

import com.example.bamboo.dto.request.user.UserCheckDTO;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class CustomException extends RuntimeException{
    private int errorCode;
    private UserCheckDTO userCheckDTO;

    public CustomException(String message, UserCheckDTO userCheckDTO, ErrorCode errorCode) {
        super(message);
        log.info("[CustomException] CustomException Error");
        log.info("Error Code[{}]", errorCode);
        this.userCheckDTO = userCheckDTO;
        this.errorCode = errorCode.getCode();
    }

    public CustomException(String message, ErrorCode errorCode) {
        super(message);
        log.warn("Error Code[{}]", errorCode.getCode());
        this.errorCode = errorCode.getCode();
    }
}