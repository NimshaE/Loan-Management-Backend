package com.inn.loan.serviceImpl;

import com.inn.loan.dao.LoanDao;
import com.inn.loan.dao.ProductDao;
import com.inn.loan.dao.SellerDao;
import com.inn.loan.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    SellerDao sellerDao;
    @Autowired
    ProductDao productDao;
    @Autowired
    LoanDao loanDao;
    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
        Map<String,Object> map = new HashMap<>();
        map.put("seller",sellerDao.count());
        map.put("product",productDao.count());
        map.put("loan",loanDao.count());
        return new ResponseEntity<>(map,HttpStatus.OK);
    }
}
