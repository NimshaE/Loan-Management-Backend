package com.inn.loan.POJO;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@NamedQuery(name = "User.findbyEmailId",query = "select u from User u where u.email=:email")

@NamedQuery(name = "User.getAllUser",query = "select new com.inn.loan.wrapper.UserWrapper(u.fullname,u.dob,u.email,u.insPlan,u.status) from User u where u.role='user'")

@NamedQuery(name = "User.updateStatus", query = "update User u set u.status=:status where u.fullname=:fullname")

@NamedQuery(name = "User.getAllAdmin",query = "select u.email from User u where u.role='admin'")

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
