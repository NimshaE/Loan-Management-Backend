package com.inn.loan.restImpl;

import com.inn.loan.POJO.Seller;
import com.inn.loan.constents.LoanConstent;
import com.inn.loan.rest.SellerRest;
import com.inn.loan.service.SellerService;
import com.inn.loan.utils.LoanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class SellerRestImpl implements SellerRest {

    @Autowired
    SellerService sellerService;

    @Override
    public ResponseEntity<String> addNewSeller(Map<String, String> requestMap) {
        try {
            return sellerService.addNewSeller(requestMap);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return LoanUtils.getResponseEntity(LoanConstent.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Seller>> getAllSeller(String filterValue) {

        try{
            return sellerService.getAllSeller(filterValue);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateSeller(Map<String, String> requestMap) {

        try{
            return sellerService.updateSeller(requestMap);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return LoanUtils.getResponseEntity(LoanConstent.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
