package com.billdiary.exception;

import com.billdiary.constant.ErrorConstants;
import lombok.Getter;

@Getter
public class BusinessRuntimeException extends RuntimeException{


    private final String errorCode;
    private final String errorMessage;

    public BusinessRuntimeException(String errorMessage){
        super(errorMessage);
        this.errorCode  = ErrorConstants.Err_Code_501;
        this.errorMessage = errorMessage;
    }

    public BusinessRuntimeException(String errorCode, String errorMessage){
        super(errorMessage);
        this.errorCode  = errorCode;
        this.errorMessage = errorMessage;
    }
}
