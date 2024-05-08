package com.effortless.effortlessmarket.domain.product.dto;

import com.effortless.effortlessmarket.domain.product.entity.Product;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ProductResponse {

    private Long id;
    private String name;
    private Integer price;
    private String description;
    private Integer quantity;
    private Integer views;
    private Integer likes;
    private Long sellerId;
    private String sellerName;
    private String brandName;
    private Long categoryId;
    private String categoryName;
    private String thumbnail;
    private LocalDateTime createdAt;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.quantity = product.getQuantity();
        this.views = product.getViews();
        this.likes = product.getLikes();
        this.thumbnail = product.getThumbnail();
        this.createdAt = product.getCreatedAt();
        this.sellerId = product.getSeller().getId();
        this.sellerName = product.getSeller().getName();
        this.brandName = product.getSeller().getBrandName();
        this.categoryId = product.getCategory().getId();
        this.categoryName = product.getCategory().getName();

    }


}
