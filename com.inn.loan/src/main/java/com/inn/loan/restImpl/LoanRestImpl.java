package com.inn.loan.restImpl;

import com.inn.loan.POJO.Loan;
import com.inn.loan.constents.LoanConstent;
import com.inn.loan.rest.LoanRest;
import com.inn.loan.service.LoanService;
import com.inn.loan.utils.LoanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class LoanRestImpl implements LoanRest {

    @Autowired
    LoanService loanService;

    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {

        try {

            return loanService.generateReport(requestMap);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return LoanUtils.getResponseEntity(LoanConstent.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Loan>> getLoans() {
        try{
            return loanService.getLoans();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
        try{
            return loanService.getPdf(requestMap);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<String> deleteBill(Integer id) {
        try{
            return loanService.deleteLoan(id);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        return LoanUtils.getResponseEntity(LoanConstent.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
