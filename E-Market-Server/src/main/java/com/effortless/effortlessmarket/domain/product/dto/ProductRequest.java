package com.effortless.effortlessmarket.domain.product.dto;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductRequest {

    private Long id;
    private String name;
    private Integer price;
    private String description;
    private Integer quantity;
    private Long categoryId;
    private Long sellerId;
}
