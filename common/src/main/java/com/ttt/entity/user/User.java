package com.ttt.entity.user;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

@Data
public class User implements Serializable {
    private BigInteger id;
    private String username;
    private String password;
    private String email;
    private String role;
    private BigDecimal height;
    private BigDecimal weight;
    private String dietPreference;
    private String allergies;
    private BigDecimal bmi;
    private String bmiStatus;
    private String gender;
    private String code;
}
