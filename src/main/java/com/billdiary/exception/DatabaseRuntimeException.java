package com.billdiary.exception;

import com.billdiary.constant.ErrorConstants;
import lombok.Getter;

@Getter
public class DatabaseRuntimeException extends  RuntimeException{

    private final String errorCode;
    private final String errorMessage;

    public DatabaseRuntimeException(String errorMessage){
        super(errorMessage);
        this.errorCode  = ErrorConstants.Err_Code_501;
        this.errorMessage = errorMessage;
    }

    public DatabaseRuntimeException(String errorCode, String errorMessage){
        super(errorMessage);
        this.errorCode  = ErrorConstants.Err_Code_501;
        this.errorMessage = errorMessage;
    }
}
