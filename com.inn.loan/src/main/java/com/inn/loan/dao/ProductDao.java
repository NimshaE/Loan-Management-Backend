package com.inn.loan.dao;

import com.inn.loan.POJO.Product;
import com.inn.loan.wrapper.ProductWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface ProductDao extends JpaRepository<Product, Integer> {
    List<ProductWrapper> getAllProducts();
   @Modifying
   @Transactional
   Integer updateProductStatus(@Param("status") String status, @Param("id") Integer id);

    List<ProductWrapper> getProductBySeller(@Param("id") Integer id);

    ProductWrapper getProductById(@Param("id") Integer id);
}
