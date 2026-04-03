package com.ttt.resp.user;

import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

@Data
public class UserCollectionResp implements Serializable {
    private BigInteger id;
    private String name;
    private String imageUrl;
    private String categoryName;
}
