package com.tappay.service.security.Exception;

import org.springframework.web.bind.annotation.ControllerAdvice;


public class MyException extends Exception{
    private int code=0;

    public MyException(String message) {
        super(message);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
