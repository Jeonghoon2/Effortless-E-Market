package com.effortless.effortlessmarket.domain.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class SaveProductRequest {

    @NotNull
    private Long sellerId;
    @NotBlank
    private String name;
    @NotNull
    private int price;
    private String description;
    @NotNull
    private int quantity;
    @NotNull
    private int isOpen;

}
