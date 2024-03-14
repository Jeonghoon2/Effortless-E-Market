package com.effortless.effortlessmarket.domain.orderDetail.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDetailResponse {

    private Long sellerId;
    private Long productId;
    private String productName;
    private Long categoryId;
    private String categoryName;
    private Integer count;
    private Integer totalPrice;

}
