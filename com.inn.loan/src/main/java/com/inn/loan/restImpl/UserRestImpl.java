package com.inn.loan.restImpl;

import com.inn.loan.constents.LoanConstent;
import com.inn.loan.rest.UserRest;
import com.inn.loan.service.UserService;
import com.inn.loan.utils.LoanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserRestImpl implements UserRest {

    @Autowired
    UserService userService;
    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        try{
            return userService.signup(requestMap);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return LoanUtils.getResponseEntity(LoanConstent.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        try {
            return userService.login(requestMap);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return LoanUtils.getResponseEntity(LoanConstent.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
