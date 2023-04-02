package com.inn.loan.service;

import com.inn.loan.POJO.Seller;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface SellerService {

    ResponseEntity<String> addNewSeller(Map<String,String> requestMap);

    ResponseEntity<List<Seller>> getAllSeller(String filterValue);

    ResponseEntity<String> updateSeller(Map<String, String> requestMap);
}
