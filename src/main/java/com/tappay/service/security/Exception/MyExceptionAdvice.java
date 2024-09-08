package com.tappay.service.security.Exception;

import kong.unirest.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MyExceptionAdvice {

    @ExceptionHandler
    public ResponseEntity<?> handleException(MyException e){
        ErrorModel errorModel=new ErrorModel();
        errorModel.setCode(HttpStatus.UNPROCESSABLE_ENTITY);
        errorModel.setMessage(e.getMessage());
        return ResponseEntity.unprocessableEntity().body(errorModel);
    }
}
