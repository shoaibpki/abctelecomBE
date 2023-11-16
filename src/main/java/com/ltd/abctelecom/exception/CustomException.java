package com.ltd.abctelecom.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomException extends RuntimeException {

    private String errorCode;
    public CustomException(String msg, String errorCode){
        super(msg);
        this.errorCode = errorCode;
    }
}
