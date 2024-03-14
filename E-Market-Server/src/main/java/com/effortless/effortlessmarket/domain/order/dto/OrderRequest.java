package com.effortless.effortlessmarket.domain.order.dto;

import com.effortless.effortlessmarket.domain.orderDetail.dto.OrderDetailRequest;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


public class OrderRequest {

    @Data
    @Builder
    public static class FromCart{
        private Long memberId;
        private List<Long> cartIds;
        private Long memberAddressId;
        private String cardName;
        private String cardNumber;
    }


    @Data
    @Builder
    public static class Order{
        private Long memberId;
        private Long memberAddressId;
        private String cardName;
        private String cardNumber;
        private List<OrderDetailRequest> orderList;
    }

}
