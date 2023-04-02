package com.inn.loan.serviceImpl;

import com.inn.loan.JWT.JwtFilter;
import com.inn.loan.POJO.Product;
import com.inn.loan.POJO.Seller;
import com.inn.loan.constents.LoanConstent;
import com.inn.loan.dao.ProductDao;
import com.inn.loan.service.ProductService;
import com.inn.loan.utils.LoanUtils;
import com.inn.loan.wrapper.ProductWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductDao productDao;

    @Autowired
    JwtFilter jwtFilter;
    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {

        try{
            if(jwtFilter.isAdmin()){
                if (validateProductMap(requestMap,false)){
                    productDao.save(getProductFromMAp(requestMap,false));
                    return LoanUtils.getResponseEntity("Product added successfully",HttpStatus.OK);
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
        return LoanUtils.getResponseEntity(LoanConstent.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    private boolean validateProductMap(Map<String, String> requestMap, boolean validateId) {

        if(requestMap.containsKey("name")){
            if((requestMap.containsKey("id") && validateId)){
                return true;
            }
            else if(!validateId){
                return true;
            }
        }
        return false;
    }
    private Product getProductFromMAp(Map<String, String> requestMap, boolean isAdd) {

        Seller seller = new Seller();
        seller.setId(Integer.parseInt(requestMap.get("sellerId")));
        Product product = new Product();

        if(isAdd){
            product.setId(Integer.parseInt(requestMap.get("id")));
        }
        else {
            product.setStatus("true");
        }
        product.setSeller(seller);
        product.setName(requestMap.get("name"));
        product.setCategory(requestMap.get("category"));
        product.setBrand(requestMap.get("brand"));
        product.setPrice(Integer.parseInt(requestMap.get("price")));
        return product;
    }
    @Override
    public ResponseEntity<List<ProductWrapper>> getAllProducts() {
        try{
            return new ResponseEntity<>(productDao.getAllProducts(),HttpStatus.OK);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
