package com.inn.loan.POJO;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@NamedQuery(name = "Loan.getAllLoans",query = "select l from Loan l order by l.id desc")

@NamedQuery(name = "Loan.getLoanByUserName",query = "select l from Loan l where l.receivedby=:fullname order by l.id desc")

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "loan")
public class Loan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "name")
    private String name;

    @Column(name = "date")
    private Date date;

    @Column(name = "email")
    private String email;

    @Column(name = "insPlan")
    private String insPlan;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "balance")
    private Integer balance;

    @Column(name = "productdetails", columnDefinition = "json")
    private String productDetail;

    @Column(name = "receivedby")
    private String receivedby;




}
