package com.inn.loan.wrapper;

import lombok.Data;

@Data
public class ProductWrapper {

    Integer id;

    String name;
    String category;
    String brand;
    Integer price;
    String status;
    Integer sellerId;
    String sellerName;

    public  ProductWrapper(){

    }
    public ProductWrapper(Integer id, String name, String category, String brand, Integer price, String status,
                          Integer sellerId, String sellerName){
        this.id = id;
        this.name = name;
        this.category = category;
        this.brand = brand;
        this.price = price;
        this.status = status;
        this.sellerId = sellerId;
        this.sellerName = sellerName;
    }
}
