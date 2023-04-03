package com.inn.loan.service;

import com.inn.loan.POJO.Loan;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface LoanService {
    ResponseEntity<String> generateReport(Map<String, Object> requestMap);

    ResponseEntity<List<Loan>> getLoans();

    ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap);

    ResponseEntity<String> deleteLoan(Integer id);
}
