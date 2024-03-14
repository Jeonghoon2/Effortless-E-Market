package com.effortless.effortlessmarket.domain.orderDetail.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDetailRequest {
    private Long productId;
    private Integer count;
}
