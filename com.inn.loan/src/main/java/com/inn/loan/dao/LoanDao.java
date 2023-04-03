package com.inn.loan.dao;

import com.inn.loan.POJO.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoanDao extends JpaRepository<Loan, Integer> {


    List<Loan> getAllLoans();

    List<Loan> getLoanByUserName(@Param("fullname") String fullname);
}
