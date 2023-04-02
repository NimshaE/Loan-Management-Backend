package com.inn.loan.dao;

import com.inn.loan.POJO.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SellerDao extends JpaRepository<Seller,Integer> {

    List<Seller> getAllSeller();
}
