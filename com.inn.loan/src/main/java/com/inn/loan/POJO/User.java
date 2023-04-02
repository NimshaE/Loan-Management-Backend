package com.inn.loan.POJO;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@NamedQuery(name = "User.findbyEmailId",query = "select u from User u where u.email=:email")


@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fullname")
    private String fullname;

    @Column(name = "dob")
    private String  dob;

    @Column(name = "email")
    private String email;

    @Column(name = "role")
    private String role;

    @Column(name = "insPlan")
    private String insPlan;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private String status;

}
