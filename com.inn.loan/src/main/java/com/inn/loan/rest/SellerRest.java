package com.inn.loan.rest;


import com.inn.loan.POJO.Seller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/seller")
public interface SellerRest {

    @PostMapping(path = "/add")
    ResponseEntity<String> addNewSeller(@RequestBody(required = true)
                                        Map<String,String> requestMap);

    @GetMapping(path = "/get")
    ResponseEntity<List<Seller>> getAllSeller(@RequestParam(required = false)
                                              String filterValue);

    @PostMapping(path = "/update")
    ResponseEntity<String> updateSeller(@RequestBody(required = true)
                                        Map<String,String> requestMap);
}
