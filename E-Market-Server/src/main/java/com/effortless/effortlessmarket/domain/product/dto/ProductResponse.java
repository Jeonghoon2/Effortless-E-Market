package com.effortless.effortlessmarket.domain.product.dto;

import com.effortless.effortlessmarket.domain.product.entity.Product;
import lombok.Data;

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
    private Long categoryId;
    private String categoryName;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.quantity = product.getQuantity();
        this.views = product.getViews();
        this.likes = product.getLikes();
        this.sellerId = product.getSeller().getId();
        this.sellerName = product.getSeller().getName();
        this.categoryId = product.getCategory().getId();
        this.categoryName = product.getCategory().getName();
    }
}
