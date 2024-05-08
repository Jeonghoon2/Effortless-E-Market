package com.effortless.effortlessmarket.domain.seller.dto;

import com.effortless.effortlessmarket.domain.seller.entity.Seller;
import lombok.Builder;
import lombok.Data;

@Data
public class SellerResponse {
    private Long id;
    private String email;
    private String name;
    private String phoneNumber;
    private String brandName;

    public SellerResponse(Seller seller) {
        this.id = seller.getId();
        this.email = seller.getEmail();
        this.name = seller.getName();
        this.phoneNumber = seller.getPhoneNumber();
        this.brandName = seller.getBrandName();
    }
}

