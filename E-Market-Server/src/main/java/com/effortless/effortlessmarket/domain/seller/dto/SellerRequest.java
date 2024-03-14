package com.effortless.effortlessmarket.domain.seller.dto;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SellerRequest {
    private Long id;
    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private String brandName;

}
