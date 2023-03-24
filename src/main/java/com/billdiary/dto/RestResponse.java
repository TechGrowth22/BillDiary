package com.billdiary.dto;

import com.billdiary.constant.ApiConstants;
import lombok.Data;

@Data
public class RestResponse {

    Object data;
    private String status;
    private String errorCode;
    private String errorMessage;

    public RestResponse(){

    }

    public RestResponse(String errorCode, String errorMessage){
        this.data = null;
        this.status = ApiConstants.STATUS_FAILED;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

}
