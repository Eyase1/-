package com.ttt.resp.user;

import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

@Data
public class UserLoginAndRegisterResp implements Serializable {
    private String token;
    private String username;
    private BigInteger id;
}
