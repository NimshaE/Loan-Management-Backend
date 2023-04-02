package com.inn.loan.serviceImpl;

import com.google.common.base.Strings;
import com.inn.loan.JWT.JwtFilter;
import com.inn.loan.POJO.Seller;
import com.inn.loan.constents.LoanConstent;
import com.inn.loan.dao.SellerDao;
import com.inn.loan.service.SellerService;
import com.inn.loan.utils.LoanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class SellerServiceImpl implements SellerService {

    @Autowired
    SellerDao sellerDao;

    @Autowired
    JwtFilter jwtFilter;
    @Override
    public ResponseEntity<String> addNewSeller(Map<String, String> requestMap) {
        try{
            if(jwtFilter.isAdmin()){
                if(validateSellerMap(requestMap,false)){
                    sellerDao.save(getSellerFromMap(requestMap,false));
                    return  LoanUtils.getResponseEntity("Seller Added Successfully.",HttpStatus.OK);
                }
            }
            else {
                return LoanUtils.getResponseEntity(LoanConstent.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return LoanUtils.getResponseEntity(LoanConstent.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    private boolean validateSellerMap(Map<String, String> requestMap, boolean validateId) {

        if(requestMap.containsKey("name")){
            if(requestMap.containsKey("id") && validateId){
                return true;
            }
            else if(!validateId){
                return true;
            }
        }
        return false;
    }

    private Seller getSellerFromMap(Map<String,String> requestMap,Boolean isAdd){
        Seller seller = new Seller();
        if(isAdd){
            seller.setId(Integer.parseInt(requestMap.get("id")));
        }
        seller.setName(requestMap.get("name"));
        return seller;
    }
    @Override
    public ResponseEntity<List<Seller>> getAllSeller(String filterValue) {
        try{
            if(!Strings.isNullOrEmpty(filterValue) && filterValue.equalsIgnoreCase("true")){
                log.info("Inside if");
                return new ResponseEntity<List<Seller>>(sellerDao.getAllSeller(),HttpStatus.OK);
            }
            return new ResponseEntity<>(sellerDao.findAll(),HttpStatus.OK);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return new ResponseEntity<List<Seller>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateSeller(Map<String, String> requestMap) {

        try {
            if(jwtFilter.isAdmin()){
                if(validateSellerMap(requestMap,true)){
                    Optional optional = sellerDao.findById(Integer.parseInt(requestMap.get("id")));
                    if(!optional.isEmpty()){
                        sellerDao.save(getSellerFromMap(requestMap,true));
                        return LoanUtils.getResponseEntity("Seller updated successfully",HttpStatus.OK);
                    }
                    else {
                        return LoanUtils.getResponseEntity("Seller ID doesn't exist",HttpStatus.OK);
                    }
                }
                return LoanUtils.getResponseEntity(LoanConstent.INVALID_DATA,HttpStatus.BAD_REQUEST);
            }
            else {
                return LoanUtils.getResponseEntity(LoanConstent.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return LoanUtils.getResponseEntity(LoanConstent.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
