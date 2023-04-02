package com.inn.loan.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class LoanUtils {

    private LoanUtils(){

    }

    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus){
        return new ResponseEntity<String>("{\"message\":\""+responseMessage+"\"}", httpStatus);
    }
}
