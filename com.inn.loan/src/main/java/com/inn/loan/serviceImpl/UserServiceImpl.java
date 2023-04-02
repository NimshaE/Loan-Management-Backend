package com.inn.loan.serviceImpl;

import com.inn.loan.JWT.CustomerUserDetailsService;
import com.inn.loan.JWT.JwtFilter;
import com.inn.loan.JWT.JwtUtil;
import com.inn.loan.POJO.User;
import com.inn.loan.constents.LoanConstent;
import com.inn.loan.dao.UserDao;
import com.inn.loan.service.UserService;
import com.inn.loan.utils.EmailUtils;
import com.inn.loan.utils.LoanUtils;
import com.inn.loan.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    CustomerUserDetailsService customerUserDetailsService;
    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    JwtFilter jwtFilter;
    @Autowired
    UserDao userDao;
    @Autowired
    EmailUtils emailUtils;
    @Override
    public ResponseEntity<String> signup(Map<String, String> requestMap) {
        log.info("Inside signup {}", requestMap);
        try{
            if(validateSignUpMap(requestMap)) {
                User user = userDao.findbyEmailId(requestMap.get("email"));
                if (Objects.isNull(user)) {
                    userDao.save(getUserFromMap(requestMap));
                    return LoanUtils.getResponseEntity("Successfully registered.", HttpStatus.OK);
                } else {
                    return LoanUtils.getResponseEntity("Email already exits", HttpStatus.BAD_REQUEST);
                }
            }

            else{
                return LoanUtils.getResponseEntity(LoanConstent.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return LoanUtils.getResponseEntity(LoanConstent.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);

    }
    private boolean validateSignUpMap(Map<String,String> requestMap){
        if(requestMap.containsKey("fullname")&&requestMap.containsKey("dob")
                && requestMap.containsKey("email") && requestMap.containsKey("password")){
            return true;
        }
        return false;
    }
    private User getUserFromMap(Map<String,String> requestMap)
    {
        User user=new User();
        user.setFullname(requestMap.get("fullname"));
        user.setDob(requestMap.get("dob"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setInsPlan(requestMap.get("insPlan"));
        user.setStatus("false");
        user.setRole("user");
        return user;
    }
    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside login");
        try{
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"),requestMap.get("password"))
            );
            if(auth.isAuthenticated()){
                if(customerUserDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")){
                    return new ResponseEntity<String>("{\"token\":\""+
                            jwtUtil.generateToken(customerUserDetailsService.getUserDetail().getEmail(),
                                    customerUserDetailsService.getUserDetail().getRole()) + "\"}",
                            HttpStatus.OK);
                }
                else {
                    return new ResponseEntity<String>("{\"message\":\""+"Wait for admin approval."+"\"}",
                            HttpStatus.BAD_REQUEST);
                }
            }
        }
        catch(Exception ex){
            log.error("{}",ex);
        }
        return new ResponseEntity<String>("{\"message\":\""+"Bad Credentials."+"\"}",
                HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try {
            if(jwtFilter.isAdmin()){
                return new ResponseEntity<>(userDao.getAllUser(),HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try{
            if (jwtFilter.isAdmin()){
                Optional<User> optional = userDao.findById(requestMap.get("fullname"));
                if(!optional.isEmpty()){
                    userDao.updateStatus(requestMap.get("status"),requestMap.get("fullname"));
                    sendMailToAllAdmin(requestMap.get("status"),optional.get().getEmail(),userDao.getAllAdmin());
                    return LoanUtils.getResponseEntity("User status updated sucessfully",HttpStatus.OK);
                }
                else {
                    return LoanUtils.getResponseEntity("User id doesn't exist",HttpStatus.OK);
                }
            }
            else {
                return LoanUtils.getResponseEntity(LoanConstent.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return LoanUtils.getResponseEntity(LoanConstent.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {
        allAdmin.remove(jwtFilter.getCurrentUser());
        if(status !=null && status.equalsIgnoreCase("true")){
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(),"Account Approved", "USER:- " + user + "\n is approved by \nADMIN:-" + jwtFilter.getCurrentUser(),allAdmin);
        }else{
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(),"Account Denied", "USER:- " + user + "\n is disabled by \nADMIN:-" + jwtFilter.getCurrentUser(),allAdmin);
        }
    }

}
