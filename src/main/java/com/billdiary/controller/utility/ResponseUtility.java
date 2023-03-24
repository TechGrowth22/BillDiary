package com.billdiary.controller.utility;

import com.billdiary.config.MessageConfig;
import com.billdiary.constant.ApiConstants;
import com.billdiary.constant.ErrorConstants;
import com.billdiary.exception.BusinessRuntimeException;
import com.billdiary.exception.DatabaseException;
import com.billdiary.exception.DatabaseRuntimeException;
import com.billdiary.dto.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseUtility {

    @Autowired
    MessageConfig messageConfig;

    public ResponseEntity handleExceptionResponse(Exception exception){
        ResponseEntity responseEntity= null;
        if(exception instanceof DatabaseException){
            RestResponse response = new RestResponse();
            response.setStatus(ApiConstants.STATUS_FAILED);
            response.setErrorCode(((DatabaseException)exception).getErrorCode());
            response.setErrorMessage(((DatabaseException)exception).getErrorMessage());
            responseEntity = new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }else if(exception instanceof BusinessRuntimeException){
            RestResponse response = new RestResponse();
            response.setStatus(ApiConstants.STATUS_FAILED);
            response.setErrorCode(((BusinessRuntimeException)exception).getErrorCode());
            response.setErrorMessage(((BusinessRuntimeException)exception).getErrorMessage());
            responseEntity = new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }else if(exception instanceof DatabaseRuntimeException){
            RestResponse response = new RestResponse();
            response.setStatus(ApiConstants.STATUS_FAILED);
            response.setErrorCode(((DatabaseRuntimeException)exception).getErrorCode());
            response.setErrorMessage(((DatabaseRuntimeException)exception).getErrorMessage());
            responseEntity = new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }else{
            RestResponse response = new RestResponse(ErrorConstants.Err_Code_101, messageConfig.getMessage(ErrorConstants.Err_Code_101));
            responseEntity = new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }
}
