package com.effortless.effortlessmarket.domain.cart.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class CartRequest {

    @Data
    @Builder
    public static class addItem{
        private Long memberId;
        private Long productId;
        private Integer count;
    }


    @Data
    public static class deleteItem{
        private List<Long> cartIds = new ArrayList<>();
    }
}
