package com.inn.loan.wrapper;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserWrapper {

    private String fullname;
    private String dob;
    private String email;
    private String insPlan;
    private String status;

    public UserWrapper(String fullname, String dob, String email, String insPlan, String status) {
        this.fullname = fullname;
        this.dob = dob;
        this.email = email;
        this.insPlan = insPlan;
        this.status = status;
    }
}
