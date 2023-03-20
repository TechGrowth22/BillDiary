package com.billdiary.exception;

import com.billdiary.constant.ErrorConstants;
import lombok.Getter;

@Getter
public class DatabaseException extends  Exception{

    private final String errorCode;
    private final String errorMessage;

    public DatabaseException(String errorMessage){
        super(errorMessage);
        this.errorCode  = ErrorConstants.Err_Code_501;
        this.errorMessage = errorMessage;
    }

    public DatabaseException(String errorCode, String errorMessage){
        super(errorMessage);
        this.errorCode  = ErrorConstants.Err_Code_501;
        this.errorMessage = errorMessage;
    }
}
