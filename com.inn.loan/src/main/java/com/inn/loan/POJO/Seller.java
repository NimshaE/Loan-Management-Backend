package com.inn.loan.POJO;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@NamedQuery(name = "Seller.getAllSeller",query = "select s from Seller s where s.id in (select p.category from Product p where p.status='true')")

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "seller")
public class Seller implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

}
